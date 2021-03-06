package BP.Task;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.springCloud.entity.JudgeRoute;
import BP.Judge.JudgeRouteManager;
import BP.Judge.JudgeTool;
import BP.Port.Emp;
import BP.Port.EmpAttr;
import BP.Port.Emps;
import BP.Tools.StringUtils;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Template.*;
import BP.Web.WebUser;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.entity.NodeTaskM;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-04 18:11
 **/
@Service
public class NodeTaskService {
    private  final Logger logger = LoggerFactory.getLogger(NodeTaskService.class);

    @Resource
    private NodeTaskManage nodeTaskManage;

    @Resource
    private GenerFlowManager generFlowManager;

    @Resource
    private JudgeRouteManager judgeRouteManager;

    public NodeTaskM getNodeTaskById(Long no){
        return nodeTaskManage.getNodeTaskById(no);
    }

    public List findNodeTaskList(NodeTaskM nodeTaskM){
        try {
            return nodeTaskManage.findNodeTaskList(nodeTaskM);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    public List findNodeTaskAllList(NodeTaskM nodeTaskM){
        try {
            return nodeTaskManage.findNodeTaskAllList(nodeTaskM);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    public Long updateNodeTask(NodeTaskM nodeTaskM){
        return nodeTaskManage.updateNodeTask(nodeTaskM);
    }

    /**
    *@Description:  完成节点任务之前检查（所有节点子流程是否完成）
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/4/2 
    */
    public boolean beforeFinishNodeTask(NodeTaskM nodeTaskM){
        NodeTaskM con=new NodeTaskM();
        con.setParentNodeTask(nodeTaskM.getNo()+"");
        List childList=nodeTaskManage.findNodeTaskList(con);
        //所有子节点任务都已经完成
        if (childList==null||childList.size()==0)
            return true;
        else
            return false;
    }
    
    /**
    *@Description: 完成节点任务，并激活下一节点任务
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public  String finishNodeTask(NodeTaskM currentTask) throws Exception{

        if (!beforeFinishNodeTask(currentTask))
            return "当前节点任务不满足发送要求！";

        //结束节点任务
        currentTask.setIsReady(3);
        currentTask.setStatus(3);
        //设置节点任务为删除状态（）
        currentTask.setYn(1);
        currentTask.setEndTime(new Date());
        nodeTaskManage.updateNodeTask(currentTask);

        //开启next节点任务
        List<NodeTaskM> nextTasks=getCanStartNodeTask(currentTask);
        boolean flag=true;
        if (nextTasks==null||nextTasks.size()==0){
            //该部分流程已经没有后续节点，结束流程（结束子流程、父流程）
            finishWork(currentTask);
        }else {
            //更新GenerFlow状态
            GenerFlow generFlow=generFlowManager.getGenerFlowById(Long.parseLong(currentTask.getWorkId()));
            String activatedNodes=generFlow.getActivatedNodes();
            activatedNodes=activatedNodes.replace(currentTask.getNodeId()+",","");
            for (NodeTaskM next:nextTasks){
                flag=flag&startNodeTask(next);
                activatedNodes+=next.getNodeId()+",";
            }
            generFlow.setActivatedNodes(activatedNodes);
            generFlowManager.updateGenerFlow(generFlow);
        }
        if (flag)
            return "发送成功，后续节点任务已经启动！";
        else 
            return "发送成功，后续节点任务未启动！";
    }

    /**
    *@Description: 结束当前节点所在流程
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/17
    */

    public  boolean finishWork(NodeTaskM currentTask){
        String workId=currentTask.getWorkId();

        NodeTaskM con=new NodeTaskM();
        con.setWorkId(workId);
        List<NodeTaskM> list=nodeTaskManage.findNodeTaskList(con);
        if (list==null||list.size()==0){
            //该work下没有未完成任务，结束该流程,
            GenerFlow currentWork=generFlowManager.getGenerFlowById(Long.parseLong(workId));
            currentWork.setYn(1);
            currentWork.setStatus(2);
            currentWork.setFinishTime(new Date());
            currentWork.setActivatedNodes("");
            generFlowManager.updateGenerFlow(currentWork);
        }
        return true;
    }

    /**
    *@Description: 决策节点根据条件选择下传任务
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    public List getCanStartNodeTask(NodeTaskM nodeTaskM){
        try{
            Node node=new Node(nodeTaskM.getNodeId());
            int runModel=node.GetValIntByKey(NodeAttr.RunModel);
            switch (runModel){
                case 9:
                    //如果存在judgeNodeId=当前节点，则说明该决策节点是匹配前置决策节点，不增加JudgeRoute信息
                    JudgeRoute con=new JudgeRoute();
                    con.setWorkId(nodeTaskM.getWorkId());
                    con.setJudgeNodeId(nodeTaskM.getNodeId());
                    List list=judgeRouteManager.findJudgeRouteList(con);
                    if (list.size()!=0){//走正常路线，获取后置连线节点
                        break;
                    }

                    List<String> nextNodes=JudgeTool.judge(nodeTaskM);
                    //记录分支信息（决策多分支，合并时使用）
                    JudgeRoute route=new JudgeRoute();
                    route.setNum(nextNodes.size());
                    route.setWorkId(nodeTaskM.getWorkId());
                    route.setJudgeNodeId(node.getJudgeNodeId()+"");
                    route.setRoutes(nextNodes.toString());
                    judgeRouteManager.insertJudgeRoute(route);

                    return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(nodeTaskM.getParentNodeTask(),nextNodes);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return getAfterNodeTask(nodeTaskM);
    }

    /**
     *@Description: 激活节点任务，同时需要激活该节点中，子流程任务（循环激活，直到激活所有子流程）
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/3/17
     */
    public boolean startNodeTask(NodeTaskM nodeTaskM){
        //启动节点任务前，判断能否启动（所有前置节点任务是否均完成）
        if (!beforeStartNodeTask(nodeTaskM))
            return false;
        
        //更新任务状态
        nodeTaskM.setIsReady(1);//可以开始
        nodeTaskM.setStatus(getTaskStatus(nodeTaskM));
        if (StringUtils.isEmpty(nodeTaskM.getExecutor())){

            // 计划时，如果没有指定负责人，则通过人员选择器进行选择执行人。
            nodeTaskM.setExecutor(getExecutor(nodeTaskM));
        }
        nodeTaskManage.updateNodeTask(nodeTaskM);

        //开启子流程任务
        List<NodeTaskM> subStartTasks=getSubFlowStartNodeTask(nodeTaskM);
        if (subStartTasks!=null) {
            for (NodeTaskM sub:subStartTasks) {
                this.startNodeTask(sub);
            }
        }
        return true;
    }


    /**
    *@Description: 获取所有子流程的开始节点任务
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/3
    */
    public List<NodeTaskM> getSubFlowStartNodeTask(NodeTaskM nodeTaskM){

        try {
            FrmSubFlow subFlow=new FrmSubFlow(Integer.valueOf(nodeTaskM.getNodeId()));
            String childFlow=subFlow.getSFDefInfo();
            if (childFlow==null||childFlow.equals("")){
                return null;
            }
            String[] childFlows=childFlow.split("%");
            List<String> childs=new ArrayList<>(childFlows.length);
            for (String childFlowNo:childFlows){
                Flow flow=new Flow(childFlowNo);
                int startNodeID=flow.getStartNodeID();
                childs.add(startNodeID+"");
            }
            return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(nodeTaskM.getNo()+"",childs);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
    /**
    *@Description: 开始节点任务前检查（检查所有前置节点是否完成，所有子流程能否启动）
     * 后续还需要增加对相应资源的判断（是否已经齐套）
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public boolean beforeStartNodeTask(NodeTaskM nodeTaskM){
        try{
            Node node=new Node(nodeTaskM.getNodeId());
            switch (node.getHisRunModel()){
                case Judge://检查是否有与该决策节点对应的前置决策节点，存在时，前置决策节点的所有分支运行到该处，才允许启动
                    JudgeRoute con=new JudgeRoute();
                    con.setWorkId(nodeTaskM.getWorkId());
                    con.setJudgeNodeId(nodeTaskM.getNodeId());
                    List<JudgeRoute> judgeRouteList=judgeRouteManager.findJudgeRouteList(con);
                    if (judgeRouteList.size()>0){
                        JudgeRoute route=judgeRouteList.get(0);
                        route.setArriveNum(route.getArriveNum()+1);
                        judgeRouteManager.updateJudgeRoute(route);
                        if (route.getArriveNum()==route.getNum()) //所有分支到齐
                            return true;
                        else
                            return false;
                    }

                default:
                    List<NodeTaskM> preList=getPreNodeTask(nodeTaskM);
                    if (preList!=null) {
                        for (NodeTaskM pre : preList) {
                            if (pre.getIsReady() != 3) {//前置节点任务存在未完成项，不允许启动该节点任务
                                return false;
                            }
                        }
                    }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return true;
    }
    
    /**
    *@Description: 获取后置节点任务 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public List getAfterNodeTask(NodeTaskM nodeTaskM){
        try {
            Directions directions=new Directions();
            
            directions.Retrieve(DirectionAttr.Node,nodeTaskM.getNodeId());
            
            List<Direction> directionList=directions.toList();
            List<String> nodeIds=new ArrayList<>(directionList.size());
            for (Direction dir:directionList){
                nodeIds.add(dir.getToNode()+"");
            }
            return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(nodeTaskM.getParentNodeTask(),nodeIds);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
    
    /**
    *@Description: 获取前置节点任务 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public List getPreNodeTask(NodeTaskM nodeTaskM){
        try {
            Directions directions=new Directions();
            
            directions.Retrieve(DirectionAttr.ToNode,nodeTaskM.getNodeId());
            
            List<Direction> directionList=directions.toList();
            List<String> nodeIds=new ArrayList<>(directionList.size());
            for (Direction dir:directionList){
                nodeIds.add(dir.getNode()+"");
            }
            return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(nodeTaskM.getParentNodeTask(),nodeIds);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }


    /**
    *@Description: 更新节点状态
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/2
    */
    public int getTaskStatus(NodeTaskM nt){
        int isReadyNt=nt.getIsReady();
        if (isReadyNt!=20&&isReadyNt!=3) {//可以开始并且未完成状态下检查(计划完成后，)
                    //判断是否逾期
            Date planStart = nt.getPlanStartTime();
            Date planEnd = nt.getPlanEndTime();
            Date current = new Date();
            Calendar calendar = Calendar.getInstance();
            int rat = 1000 * 60 * 60 * 24;

            if (isReadyNt==1) {//未开始，
                int dayNumS = (int) ((planStart.getTime() - current.getTime()) / rat);
                if (dayNumS < 0)
                    return 4;//逾期开始
                else if (dayNumS < 3)
                    return 5;//三天内警告
            } else {//已经开始
                int dayNumE = (int) ((planEnd.getTime() - current.getTime()) / rat);
                if (dayNumE < 0)
                    return 7;//逾期结束
                else if (dayNumE < 3)
                    return 8;//警告结束
            }
            return 6;//正常
        }
        return nt.getIsReady();
    }
    

    public Long insertNodeTask(NodeTaskM nodeTaskM){
        return nodeTaskManage.insertNodeTask(nodeTaskM);
    }

    /**
    *@Description: 获取GenerFlow下层次不超过depth深度的所有节点任务进度
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/6
    */
    public List<JSONObject> getGantData(GenerFlow generFlow, int depth){
        List<JSONObject> data=new LinkedList<>();

        JSONObject temp=new JSONObject();
        temp.put("name","实例"+generFlow.getNo());
        temp.put("id",generFlow.getNo()+"");
        temp.put("owner",generFlow.getCreator());
        data.add(temp);

        NodeTaskM con=new NodeTaskM();
        con.setWorkId(generFlow.getWorkId()+"");
        List<NodeTaskM> list=nodeTaskManage.findNodeTaskAllList(con);
        transToGant(list,generFlow.getNo()+"",data,depth,0);

        return data;
    }

    public void transToGant(List<NodeTaskM> children,String parentId,List<JSONObject> data,int depth,int curDepth){

        if (children==null)
            return;

        NodeTaskM con=new NodeTaskM();

        for (NodeTaskM nodeTaskM:children){
            JSONObject point=new JSONObject();
            String id=nodeTaskM.getNo()+"-"+nodeTaskM.getNodeName();
            point.put("name",id);
            point.put("id",id);

            //获取前置节点任务
            List<NodeTaskM> preNodeTasks=getPreNodeTask(nodeTaskM);
            if (preNodeTasks!=null){
                List<String> dependencyList=new ArrayList<>(preNodeTasks.size());
                for (NodeTaskM pre:preNodeTasks){
                    dependencyList.add(pre.getNo()+"-"+pre.getNodeName());
                }
                point.put("dependency",dependencyList);
            }

            int shiCha=8 * 60 * 60 * 1000;//时间戳会存在时差问题

            int isReady=nodeTaskM.getIsReady();
            if (isReady==1||isReady==2)//已经开始
                point.put("start",nodeTaskM.getStartTime().getTime()+shiCha);
            else
                point.put("start",nodeTaskM.getPlanStartTime().getTime()+shiCha);

            if (nodeTaskM.getIsReady()==3)//已经结束
                point.put("end",nodeTaskM.getEndTime().getTime()+shiCha);
            else
                point.put("end",nodeTaskM.getPlanEndTime().getTime()+shiCha);

            JSONObject completed=new JSONObject();

            Double amount = (nodeTaskM.getUseTime()+0.0)/nodeTaskM.getTotalTime();
            if (nodeTaskM.getIsReady()==3)//已经完成时，不按用时计算
                amount=1.0;
            BigDecimal bd = new BigDecimal(amount);
            amount = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            completed.put("amount",amount);
            point.put("completed",completed);
            point.put("parent",parentId);
            point.put("owner",nodeTaskM.getExecutor());

            data.add(point);

            //深度满足，添加子流程信息
            if (curDepth<depth){
                //添加子流程任务信息
                con.setParentNodeTask(nodeTaskM.getNo()+"");
                List<NodeTaskM> nextChildren=nodeTaskManage.findNodeTaskAllList(con);
                transToGant(nextChildren,id,data,depth,curDepth+1);
            }

        }
    }

    public String getExecutor(NodeTaskM nodeTaskM){
        String nodeId=nodeTaskM.getNodeId();
        String executor=null;

        List<String> employeeNos=new ArrayList<>();//所有可选人员
        try {
            executor= WebUser.getNo();//默认自身
            Node node = new Node(nodeId);
            DataTable dt;
            switch (node.getHisDeliveryWay()) {
                case NoSelect:
                    break;
                case ByStation:
                    String sql = "SELECT No, Name FROM Port_Emp WHERE No IN (SELECT A.FK_Emp FROM " + BP.WF.Glo.getEmpStation() + " A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + nodeId + ")";
                    dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
                    if (dt.Rows.size()!=0) {
                        for (DataRow dr : dt.Rows) {
                            employeeNos.add(dr.getValue(0).toString());
                        }
                    }
                    break;
                case ByDept:
                    NodeDepts nds=new NodeDepts();
                    nds.Retrieve(NodeEmpAttr.FK_Node, nodeId);
                    for (NodeDept nodeDept:nds.ToJavaList()){
                        Emps emps=new Emps();
                        emps.Retrieve(EmpAttr.FK_Dept,nodeDept.getFK_Dept());
                        for (Emp emp:emps.ToJavaList()){
                            employeeNos.add(emp.getNo());
                        }
                    }
                    break;
                case ByBindEmp:
                    NodeEmps nes = new NodeEmps();
                    nes.Retrieve(NodeEmpAttr.FK_Node, nodeId);
                    for (NodeEmp ne : nes.ToJavaList())
                        employeeNos.add(ne.getFK_Emp());
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        //此处后续可以增加调度算法进行人员选择（）此时返回一个随机人员
        Random random=new Random();
        if (employeeNos.size()>0) {
            int pos = random.nextInt(employeeNos.size());
            executor=employeeNos.get(pos);
        }
        return executor;
    }

    /**
    *@Description: 构建节点任务之间的拓扑关系
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/20
    */
    public boolean updateNodeTaskPreAfter(String startNodeId,Long workId){
        NodeTaskM con=new NodeTaskM();
        con.setWorkId(workId+"");
        //获取当前流程的所有节点任务
        List<NodeTaskM> nodeTaskMAllTask=findNodeTaskList(con);
        Map<String,NodeTaskM> map=new HashMap<>();
        for (NodeTaskM temp:nodeTaskMAllTask)
            map.put(temp.getNodeId(),temp);

        //遍历更新所有节点任务
        for (String nodeId:map.keySet()){
            NodeTaskM nodeTaskM=map.get(nodeId);
            String pre="";
            List<NodeTaskM> befores=getPreNodeTask(nodeTaskM);
            if (befores!=null){
                for (NodeTaskM before:befores){
                    pre=pre+before.getNo()+",";
                }
            }
            nodeTaskM.setPreNodeTask(pre);

            String after="";
            List<NodeTaskM> nexts=getAfterNodeTask(nodeTaskM);
            if (nexts!=null){
                for (NodeTaskM next:nexts)
                    after=after+next.getNo()+",";
            }
            nodeTaskM.setNextNodeTask(after);

            nodeTaskManage.updateNodeTask(nodeTaskM);
        }

        return true;
    }

}
