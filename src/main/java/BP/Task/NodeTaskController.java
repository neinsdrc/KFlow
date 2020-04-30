package BP.Task;

import BP.Tools.StringUtils;
import BP.Web.WebUser;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.tool.PageTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-04 18:06
 **/
@Controller()
@RequestMapping("nodeTask")
public class NodeTaskController {

    private final Logger logger = LoggerFactory.getLogger(NodeTaskController.class);

    @Resource
    private NodeTaskService nodeTaskService;

    @Resource
    private GenerFlowService generFlowService;
    /**
    *@Description: 查询节点任务列表（条件查询）
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/4
    */
    @RequestMapping("/getNodeTasks")
    public void getNodeTaskList(HttpServletRequest request, HttpServletResponse response){
        try {
            NodeTaskM con=new NodeTaskM();
            String executor=request.getParameter("executor");
            if (executor!=null&&!executor.equals(""))
                con.setExecutor(executor);
            else
                con.setExecutor(WebUser.getNo());
            String nodeTaskNo=request.getParameter("nodeTaskNo");
            if (!StringUtils.isEmpty(nodeTaskNo))
                con.setNo(Long.parseLong(nodeTaskNo));

            String workId=request.getParameter("workId");
            if (!StringUtils.isEmpty(workId))
                con.setWorkId(workId);

            String flowNo=request.getParameter("flowNo");
            if (!StringUtils.isEmpty(flowNo))
                con.setFlowId(flowNo);

            String status=request.getParameter("status");
            if (!StringUtils.isEmpty(status))
                con.setStatus(Integer.parseInt(status));

            List nodeTaskMList=nodeTaskService.findNodeTaskAllList(con);
            PageTool.TransToResultList(nodeTaskMList,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }


    @RequestMapping("/getNodeTaskByNo")
    @ResponseBody
    public Object getNodeTask(String no){
        NodeTask nodeTask=new NodeTask();
        nodeTask.setNo(no);
        try {
            nodeTask.RetrieveByNo();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return nodeTask.ToJson();
    }

    @RequestMapping("/startNodeTask")
    public void startNodeTask(Long no){
        try {
            NodeTaskM nodeTaskM=nodeTaskService.getNodeTaskById(no);
            if (nodeTaskM.getIsReady()==1){
                nodeTaskM.setStartTime(new Date());
                nodeTaskM.setIsReady(2);
                nodeTaskM.setStatus(2);
                nodeTaskService.updateNodeTask(nodeTaskM);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
    *@Description: 完成当前节点任务，并激活下一节点任务
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/18
    */
    @RequestMapping("sendNode")
    @ResponseBody
    public JSONObject sendNode(Long nodeTaskNo){
        JSONObject result=new JSONObject();
        try {
            //完善NodeTask信息
            NodeTaskM currentTask=nodeTaskService.getNodeTaskById(nodeTaskNo);

            //完成节点任务，并将节点任务进行下发（）
            String meg=nodeTaskService.finishNodeTask(currentTask);
            result.put("message",meg);
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
    *@Description: 人为更新流程引擎中所有节点任务状态
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/2
    */
    @RequestMapping("updateTasksStatus")
    @ResponseBody
    public JSONObject updateTasksStatus(@RequestBody int type){
        NodeTaskM con=new NodeTaskM();
        try {
            if (type==1)
                con.setExecutor(WebUser.getNo());//更新所执行任务
            List<NodeTaskM> nodeTaskMList=nodeTaskService.findNodeTaskList(con);
            for (NodeTaskM temp:nodeTaskMList){
                int status=nodeTaskService.getTaskStatus(temp);
                temp.setStatus(status);
                nodeTaskService.updateNodeTask(temp);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }


    /**
    *@Description: 查询该WorkId下面所有任务进展信息(单个实例层级)
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/5
    */
    public JSONObject getNodeTasksByWorkId(Long generFlowNo){
        return null;
    }

    /**
     *@Description: 查询该WorkId下面所有任务进展信息(多粒度实例层级)
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/4/5
     */
    public JSONObject getNodeTasksByGroupWorkId(String groupWorkId){
        return null;
    }


    @RequestMapping("getNodeTaskGantData")
    @ResponseBody
    public JSONObject getNodeTaskGantData(Long generFlowNo,int depth){
        List<JSONObject> seriesList=new ArrayList<>();

        JSONObject seriesN=new JSONObject();
        seriesN.put("name","实例"+generFlowNo);
        GenerFlow generFlow=generFlowService.getGenerFlow(generFlowNo);
        List<JSONObject> data= nodeTaskService.getGantData(generFlow,depth);
        seriesN.put("data",JSONArray.fromObject(data));
        seriesList.add(seriesN);

        JSONObject xAxis=new JSONObject();
        xAxis.put("currentDateIndicator",true);

        int shiCha=(8+8) * 60 * 60 * 1000;//时差，
        xAxis.put("min",generFlow.getCreateTime().getTime()+shiCha);
        if (generFlow.getStatus()!=2) {
            Date cur=new Date();
            xAxis.put("max", cur.getTime() + shiCha);
        }else
            xAxis.put("max",generFlow.getFinishTime().getTime()+shiCha);

        JSONObject result=new JSONObject();
        try {
            result.put("series", JSONArray.fromObject(seriesList));
            result.put("xAxis",xAxis);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    @RequestMapping("checkNodeTaskIsFinish")
    @ResponseBody
    public boolean checkNodeTaskIsFinish(@RequestParam Long no){
        NodeTaskM nodeTaskM=nodeTaskService.getNodeTaskById(no);
        if (nodeTaskM.getIsReady()==3){
            return false;
        }
        return true;
    }
}
