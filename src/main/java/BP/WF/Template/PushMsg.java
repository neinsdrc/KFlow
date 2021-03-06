package BP.WF.Template;

import BP.DA.*;
import BP.En.Entity;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.Row;
import BP.Sys.EventListOfNode;
import BP.Tools.StringHelper;
import BP.WF.*;
import BP.WF.Port.WFEmp;
import BP.Web.WebUser;

/** 
 消息推送
 
*/
public class PushMsg extends EntityMyPK
{

		
	/** 
	 流程编号
	 
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(PushMsgAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(PushMsgAttr.FK_Flow, value);
	}
	/** 
	 事件
	 
	*/
	public final String getFK_Event()
	{
		return this.GetValStringByKey(PushMsgAttr.FK_Event);
	}
	public final void setFK_Event(String value)
	{
		this.SetValByKey(PushMsgAttr.FK_Event, value);
	}
	/** 
	 推送方式.
	 
	*/
	public final int getPushWay()
	{
		return this.GetValIntByKey(PushMsgAttr.PushWay);
	}
	public final void setPushWay(int value)
	{
		this.SetValByKey(PushMsgAttr.PushWay, value);
	}
	/** 
	节点
	 
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(PushMsgAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		this.SetValByKey(PushMsgAttr.FK_Node, value);
	}
	public final String getPushDoc()
	{
		String s = this.GetValStringByKey(PushMsgAttr.PushDoc);
		if (StringHelper.isNullOrEmpty(s) == true)
		{
			s = "";
		}
		return s;
	}
	public final void setPushDoc(String value)
	{
		this.SetValByKey(PushMsgAttr.PushDoc, value);
	}
	public final String getTag()
	{
		String s = this.GetValStringByKey(PushMsgAttr.Tag);
		if (StringHelper.isNullOrEmpty(s) == true)
		{
			s = "";
		}
		return s;
	}
	public final void setTag(String value)
	{
		this.SetValByKey(PushMsgAttr.Tag, value);
	}

		///#endregion


		///#region 事件消息.
	/** 
	 邮件推送方式
	 
	*/
	public final int getMailPushWay()
	{
		return this.GetValIntByKey(PushMsgAttr.MailPushWay);
	}
	public final void setMailPushWay(int value)
	{
		this.SetValByKey(PushMsgAttr.MailPushWay, value);
	}
	/** 
	 推送方式Name
	 
	*/
	public final String getMailPushWayText()
	{
		if (this.getFK_Event().equals(EventListOfNode.WorkArrive))
		{
			if (this.getMailPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getMailPushWay() == 1)
			{
				return "发送给当前节点的所有处理人";
			}

			if (this.getMailPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
		{
			if (this.getMailPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getMailPushWay() == 1)
			{
				return "发送给下一个节点的所有接受人";
			}

			if (this.getMailPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
			if (this.getMailPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getMailPushWay() == 1)
			{
				return "发送给被退回的节点处理人";
			}

			if (this.getMailPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		return "未知";
	}
	/** 
	 邮件地址
	 
	*/
	public final String getMailAddress()
	{
		return this.GetValStringByKey(PushMsgAttr.MailAddress);
	}
	public final void setMailAddress(String value)
	{
		this.SetValByKey(PushMsgAttr.MailAddress, value);
	}
	/** 
	 邮件标题.
	 
	*/
	public final String getMailTitle()
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailTitle);
		if (StringHelper.isNullOrEmpty(str) == false)
		{
			return str;
		}

		if (this.getFK_Event().equals(EventListOfNode.WorkArrive))
		{
				return "新工作{{Title}},发送人WebUser.No,@WebUser.Name";
		}
		else if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
		{
				return "新工作{{Title}},发送人WebUser.No,@WebUser.Name";
		}
		else if (this.getFK_Event().equals(EventListOfNode.ShitAfter))
		{
				return "移交来的新工作{{Title}},移交人WebUser.No,@WebUser.Name";
		}
		else if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
				return "被退回来{{Title}},退回人WebUser.No,@WebUser.Name";
		}
		else if (this.getFK_Event().equals(EventListOfNode.UndoneAfter))
		{
				return "工作被撤销{{Title}},发送人WebUser.No,@WebUser.Name";
		}
		else if (this.getFK_Event().equals(EventListOfNode.AskerReAfter))
		{
				return "加签新工作{{Title}},发送人WebUser.No,@WebUser.Name";
		}
		else if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter))
		{
				return "流程{{Title}}已经结束,处理人WebUser.No,@WebUser.Name";
		}
		else
		{
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
		}
	}
	/** 
	 Email节点s
	 
	*/
	public final String getMailNodes()
	{
		return this.GetValStringByKey(PushMsgAttr.MailNodes);
	}
	public final void setMailNodes(String value)
	{
		this.SetValByKey(PushMsgAttr.MailNodes, value);
	}
	/** 
	 邮件标题
	 
	*/
	public final String getMailTitle_Real()
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailTitle);
		return str;
	}
	public final void setMailTitle_Real(String value)
	{
		this.SetValByKey(PushMsgAttr.MailTitle, value);
	}
	/** 
	 邮件内容
	 
	*/
	public final String getMailDoc_Real()
	{
		return this.GetValStrByKey(PushMsgAttr.MailDoc);
	}
	public final void setMailDoc_Real(String value)
	{
		this.SetValByKey(PushMsgAttr.MailDoc, value);
	}
	public final String getMailDoc()
	{
		String str = this.GetValStrByKey(PushMsgAttr.MailDoc);
		if (StringHelper.isNullOrEmpty(str) == false)
		{
			return str;
		}

		if (this.getFK_Event().equals(EventListOfNode.WorkArrive))
		{
				str += "\t\n您好:";
				str += "\t\n    有新工作{{Title}}需要您处理, 点击这里打开工作报告{Url} .";
				str += "\t\n致! ";
				str += "\t\n    WebUser.No, @WebUser.Name";
				str += "\t\n    @RDT";
		}
		else if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
		{
				str += "\t\n您好:";
				str += "\t\n    有新工作{{Title}}需要您处理, 点击这里打开工作{Url} .";
				str += "\t\n致! ";
				str += "\t\n    WebUser.No, @WebUser.Name";
				str += "\t\n    @RDT";
		}
		else if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
				str += "\t\n您好:";
				str += "\t\n    工作{{Title}}被退回来了, 点击这里打开工作报告{Url} .";
				str += "\t\n    退回意见: \t\n ";
				str += "\t\n    {  @ReturnMsg }";
				str += "\t\n 致! ";
				str += "\t\n    WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
		}
		else if (this.getFK_Event().equals(EventListOfNode.ShitAfter))
		{
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
		}
		else if (this.getFK_Event().equals(EventListOfNode.UndoneAfter))
		{
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作{{Title}}, 点击这里打开工作报告{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
		}
		else if (this.getFK_Event().equals(EventListOfNode.AskerReAfter)) //加签.
		{
				str += "\t\n您好:";
				str += "\t\n    移交给您的工作{{Title}}, 点击这里打开报告{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
		}
		else if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter)) //流程结束后.
		{
				str += "\t\n您好:";
				str += "\t\n    工作{{Title}}已经结束, 点击这里打开工作报告{Url} .";
				str += "\t\n 致! ";
				str += "\t\n    WebUser.No,@WebUser.Name";
				str += "\t\n    @RDT";
		}
		else
		{
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
		}
		return str;
	}
	/** 
	 短信接收人字段
	 
	*/
	public final String getSMSField()
	{
		return this.GetValStringByKey(PushMsgAttr.SMSField);
	}
	public final void setSMSField(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSField, value);
	}
	public final String getSMSNodes()
	{
		return this.GetValStringByKey(PushMsgAttr.SMSNodes);
	}
	public final void setSMSNodes(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSNodes, value);
	}
	/** 
	 短信提醒方式
	 
	*/
	public final int getSMSPushWay()
	{
		return this.GetValIntByKey(PushMsgAttr.SMSPushWay);
	}
	public final void setSMSPushWay(int value)
	{
		this.SetValByKey(PushMsgAttr.SMSPushWay, value);
	}

	/**
	 * 按照SQL计算发送
	 * @return
	 */
	public final String getBySQL()
	{
		return this.GetValStringByKey(PushMsgAttr.BySQL);
	}
	public final void setBySQL(String value)
	{
		this.SetValByKey(PushMsgAttr.BySQL, value);
	}

	/**
	 * 按照指定的人员计算
	 * @return
	 */
	public final String getByEmps()
	{
		return this.GetValStringByKey(PushMsgAttr.ByEmps);
	}
	public final void setByEmps(String value)
	{
		this.SetValByKey(PushMsgAttr.ByEmps, value);
	}

	public final String getSMSPushModel()
	{
		return this.GetValStringByKey(PushMsgAttr.SMSPushModel);
	}
	public final void setSMSPushModel(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSPushModel, value);
	}
	/** 
	 发送消息标签
	 
	*/
	public final String getSMSPushWayText()
	{
		if (this.getFK_Event().equals(EventListOfNode.WorkArrive))
		{
			if (this.getSMSPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getSMSPushWay() == 1)
			{
				return "发送给当前节点的所有处理人";
			}

			if (this.getSMSPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.SendSuccess))
		{
			if (this.getSMSPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getSMSPushWay() == 1)
			{
				return "发送给下一个节点的所有接受人";
			}

			if (this.getSMSPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
			if (this.getSMSPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getSMSPushWay() == 1)
			{
				return "发送给被退回的节点处理人";
			}

			if (this.getSMSPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter))
		{
			if (this.getSMSPushWay() == 0)
			{
				return "不发送";
			}

			if (this.getSMSPushWay() == 1)
			{
				return "发送给所有节点处理人";
			}

			if (this.getSMSPushWay() == 2)
			{
				return "向指定的字段发送";
			}
		}

		return "未知";
	}
	/** 
	 短信模版内容
	 
	*/
	public final String getSMSDoc_Real()
	{
		String str = this.GetValStrByKey(PushMsgAttr.SMSDoc);
		return str;
	}
	public final void setSMSDoc_Real(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSDoc, value);
	}
	/** 
	 短信模版内容
	 
	*/
	public final String getSMSDoc()
	{
		String str = this.GetValStrByKey(PushMsgAttr.SMSDoc);
		if (StringHelper.isNullOrEmpty(str) == false)
		{
			return str;
		}

		if (this.getFK_Event().equals(EventListOfNode.WorkArrive) || this.getFK_Event().equals(EventListOfNode.SendSuccess))
		{
				str = "有新工作{{Title}}需要您处理, 发送人:WebUser.No, @WebUser.Name,打开{Url} .";
		}
		else if (this.getFK_Event().equals(EventListOfNode.ReturnAfter))
		{
				str = "工作{{Title}}被退回,退回人:WebUser.No, @WebUser.Name,打开{Url} .";
		}
		else if (this.getFK_Event().equals(EventListOfNode.ShitAfter))
		{
				str = "移交工作{{Title}},移交人:WebUser.No, @WebUser.Name,打开{Url} .";
		}
		else if (this.getFK_Event().equals(EventListOfNode.UndoneAfter))
		{
				str = "工作撤销{{Title}},撤销人:WebUser.No, @WebUser.Name,打开{Url}.";
		}
		else if (this.getFK_Event().equals(EventListOfNode.AskerReAfter)) //加签.
		{
				str = "工作加签{{Title}},加签人:WebUser.No, @WebUser.Name,打开{Url}.";
		}
		else if (this.getFK_Event().equals(EventListOfNode.FlowOverAfter)) //加签.
		{
				str = "流程{{Title}}已经结束,最后处理人:WebUser.No, @WebUser.Name,打开{Url}.";
		}
		else
		{
				throw new RuntimeException("@该事件类型没有定义默认的消息模版:" + this.getFK_Event());
		}
		return str;
	}
	public final void setSMSDoc(String value)
	{
		this.SetValByKey(PushMsgAttr.SMSDoc, value);
	}

		///#endregion


		
	/** 
	 消息推送
	 
	*/
	public PushMsg()
	{
	}
	/** 
	 重写基类方法
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_PushMsg", "消息推送");

		map.AddMyPK();

		map.AddTBString(PushMsgAttr.FK_Flow, null, "流程", true, false, 0, 3, 10);
		map.AddTBInt(PushMsgAttr.FK_Node, 0, "节点", true, false);

		map.AddTBString(PushMsgAttr.FK_Event, null, "事件类型", true, false, 0, 15, 10);


			///#region 将要删除.
		map.AddDDLSysEnum(PushMsgAttr.PushWay, 0, "推送方式", true, false, PushMsgAttr.PushWay, "@0=按照指定节点的工作人员@1=按照指定的工作人员@2=按照指定的工作岗位@3=按照指定的部门@4=按照指定的SQL@5=按照系统指定的字段");
			//设置内容.
		map.AddTBString(PushMsgAttr.PushDoc, null, "推送保存内容", true, false, 0, 3500, 10);
		map.AddTBString(PushMsgAttr.Tag, null, "Tag", true, false, 0, 500, 10);
		//@0=站内消息@1=短信@2=钉钉@3=微信@4=即时通
		map.AddTBString(PushMsgAttr.SMSPushModel, null, "短消息发送设置", true, false, 0, 50, 10);
			///#endregion 将要删除.


			///#region 短信.
		map.AddTBInt(PushMsgAttr.SMSPushWay, 0, "短信发送方式", true, true);
		map.AddTBString(PushMsgAttr.SMSField, null, "短信字段", true, false, 0, 100, 10);
		map.AddTBStringDoc(PushMsgAttr.SMSDoc, null, "短信内容模版", true, false, true);
		map.AddTBString(PushMsgAttr.SMSNodes, null, "SMS节点s", true, false, 0, 100, 10);

		map.AddTBString(PushMsgAttr.BySQL, null, "按照SQL计算", true, false, 0, 500, 10);
		map.AddTBString(PushMsgAttr.ByEmps, null, "发送给指定的人员", true, false, 0, 100, 10);

			///#endregion 短信.


			///#region 邮件.
		map.AddTBInt(PushMsgAttr.MailPushWay, 0, "邮件发送方式",true, true);
		map.AddTBString(PushMsgAttr.MailAddress, null, "邮件字段", true, false, 0, 100, 10);
		map.AddTBString(PushMsgAttr.MailTitle, null, "邮件标题模版", true, false, 0, 200, 20, true);
		map.AddTBStringDoc(PushMsgAttr.MailDoc, null, "邮件内容模版", true, false, true);
		map.AddTBString(PushMsgAttr.MailNodes, null, "Mail节点s", true, false, 0, 100, 10);

			///#endregion 邮件.

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 生成提示信息.
	 @return 
	*/
	private String generAlertMessage=null;

	/** 
	 执行消息发送
	 
	 @param currNode 当前节点
	 @param en 数据实体
	 @param atPara 参数
	 @param objs 发送返回对象
	 @param jumpToNode 跳转到的节点
	 @param jumpToEmps 跳转到的人员
	 @return 执行成功的消息
	 * @throws Exception 
	*/
	public final String DoSendMessage(Node currNode, Entity en, String atPara, SendReturnObjs objs, Node jumpToNode, String jumpToEmps) throws Exception
	{
		if (en == null)
			return "";

		//处理参数.
		Row r = en.getRow();
		try
		{
			//系统参数.
			r.put("FK_MapData", en.getClassID());
		}
		catch (java.lang.Exception e)
		{
			r.put("FK_MapData", en.getClassID());
		}

		if (atPara != null)
		{
			AtPara ap = new AtPara(atPara);
			for (String s : ap.getHisHT().keySet())
			{
				try
				{
					r.put(s, ap.GetValStrByKey(s));
				}
				catch (java.lang.Exception e2)
				{
					r.put(s, ap.GetValStrByKey(s));
				}
			}
		}

		//生成标题.
		long workid = Long.parseLong(en.getPKVal().toString());
		String title = "标题";
		if (en.getRow().containsKey("Title") == true)
		{
			title = en.GetValStringByKey("Title"); // 获得工作标题.
			if(DataType.IsNullOrEmpty(title))
			{
				title = BP.DA.DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + en.getPKVal(), "标题");
			}
		}
		else
		{
			title = BP.DA.DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + en.getPKVal(), "标题");
		}

		//生成URL.
		String hostUrl = BP.WF.Glo.getHostURL();
		String sid = "{EmpStr}_" + workid + "_" + currNode.getNodeID() + "_" + DataType.getCurrentDate();
		String openWorkURl = hostUrl + "WF/Do.htm?DoType=OF&SID=" + sid;
		openWorkURl = openWorkURl.replace("//", "/");
		openWorkURl = openWorkURl.replace("//", "/");
		///#endregion

		// 有可能是退回信息. 翻译.
		if (jumpToEmps == null)
		{
			if (atPara != null)
			{
				AtPara ap = new AtPara(atPara);
				jumpToEmps = ap.GetValStrByKey("SendToEmpIDs");
			}
		}
		//发送消息
		String msg = this.SendMessage(title,en,currNode,workid,jumpToEmps,openWorkURl,objs,r);
		//发送短消息.
		///String msg1 = this.SendShortMessageToSpecNodes(title, openWorkURl, en, currNode, workid, objs, null, jumpToEmps);
		//发送邮件.
		//String msg2 = this.SendEmail(title, openWorkURl, en, jumpToEmps, currNode, workid, objs, r);
		return msg;
	}

	/**
	 *
	 * @param title 标题
	 * @param en 数据实体
	 * @param currNode 当前节点
	 * @param workid 流程WORKID
	 * @param jumpToEmps 下一个节点的接收人
	 * @param openUrl 链接的URL
	 * @param objs
	 * @param r
	 * @return
	 * @throws Exception
	 */
	private String SendMessage(String title,Entity en,Node currNode,long workid,String jumpToEmps,
							   String openUrl, SendReturnObjs objs,Row r) throws Exception
	{
		//不启用消息
		if (this.getSMSPushWay() == 0)
			return "";
		String generAlertMessage = ""; //定义要返回的提示消息.
		String mailTitle = this.getMailTitle();// 邮件标题
		String smsDoc = this.getSMSDoc();//消息模板

		//begin 邮件标题
		mailTitle = this.getMailTitle();
		mailTitle = mailTitle.replace("{Title}", title);
		mailTitle = mailTitle.replace("@WebUser.No", WebUser.getNo());
		mailTitle = mailTitle.replace("@WebUser.Name", WebUser.getName());

		//end 邮件标题

		//begin 处理消息内容
		smsDoc = smsDoc.replace("{Title}", title);
		smsDoc = smsDoc.replace("{Url}", openUrl);
		smsDoc = smsDoc.replace("@WebUser.No", WebUser.getNo());
		smsDoc = smsDoc.replace("@WebUser.Name", WebUser.getName());
		smsDoc = smsDoc.replace("@WorkID", en.getPKVal().toString());
		smsDoc = smsDoc.replace("@OID", en.getPKVal().toString());

		/*如果仍然有没有替换下来的变量.*/
		if (smsDoc.contains("@") == true)
			smsDoc = BP.WF.Glo.DealExp(smsDoc, en, null);

		if (this.getFK_Event() == BP.Sys.EventListOfNode.ReturnAfter)
		{
			//获取退回原因
			Paras ps = new Paras();
			ps.SQL = "SELECT BeiZhu,ReturnerName,IsBackTracking FROM WF_ReturnWork WHERE WorkID=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "WorkID  ORDER BY RDT DESC";
			ps.Add(ReturnWorkAttr.WorkID, Long.parseLong(en.getPKVal().toString()));
			DataTable retunWdt = DBAccess.RunSQLReturnTable(ps);
			if (retunWdt.Rows.size() != 0)
			{
				String returnMsg = retunWdt.Rows.get(0).getValue("BeiZhu").toString();
				String returner = retunWdt.Rows.get(0).getValue("ReturnerName").toString();
				smsDoc = smsDoc.replace("ReturnMsg", returnMsg);
			}
		}
		// end 处理消息内容

		// begin 消息发送
		String toEmpIDs = "";
		// begin表单字段作为接受人
		if (this.getSMSPushWay() == 2)
		{
			/*从字段里取数据. */
			String toEmp = r.get(this.getSMSField()).toString();
			//修改内容
			smsDoc = smsDoc.replace("{EmpStr}", toEmp);
			openUrl = openUrl.replace("{EmpStr}", toEmp);

			//发送消息
			BP.WF.Dev2Interface.Port_SendMessage(toEmp, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), openUrl, this.getSMSPushModel(),null,null);
			return "@已向:{" + toEmp + "}发送提醒信息.";
		}
		// end表单字段作为接受人

		//begin 如果发送给指定的节点处理人,就计算出来直接退回,任何方式的处理人都是一致的.
		if (this.getSMSPushWay() == 3)
		{
			/*如果向指定的字段作为发送邮件的对象, 从字段里取数据. */
			String[] nodes = this.getSMSNodes().split(",");

			String msg = "";
			for(String nodeID : nodes)
			{
				if (DataType.IsNullOrEmpty(nodeID) == true)
					continue;

				String sql = "SELECT EmpFromT AS Name,EmpFrom AS No FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track A  WHERE  A.ActionType=1 AND A.WorkID=" + workid + " AND A.NDFrom=" + nodeID ;
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 0)
					continue;

				for(DataRow dr : dt.Rows)
				{
					String empName = dr.getValue("Name").toString();
					String empNo = dr.getValue("No").toString();


					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					String smsDocReal = smsDoc;
					smsDocReal = smsDocReal.replace("{EmpStr}", empName);
					openUrl = openUrl.replace("{EmpStr}", empNo);

					String paras = "@FK_Flow=" + this.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + this.getFK_Node();

					//发送消息
					BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), openUrl, this.getSMSPushModel(),null,null);
					//处理短消息.
					toEmpIDs += empName + ",";
				}
			}
			return "@已向:{" + toEmpIDs + "}发送了短消息提醒.";
		}
		//end 如果发送给指定的节点处理人, 就计算出来直接退回, 任何方式的处理人都是一致的.

		// begin 按照SQL计算
		if(this.getSMSPushWay() == 4)
		{
			String bySQL = this.getBySQL();
			if(DataType.IsNullOrEmpty(bySQL) == true)
			{
				return "按照指定的SQL发送消息，SQL数据不能为空";
			}
			bySQL = bySQL.replace("~", "'");
			//替换SQL中的参数
			bySQL = bySQL.replace("@WebUser.No", WebUser.getNo());
			bySQL = bySQL.replace("@WebUser.Name", WebUser.getName());
			bySQL = bySQL.replace("@WebUser.FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
			bySQL = bySQL.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
			bySQL = bySQL.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
			/*如果仍然有没有替换下来的变量.*/
			if (bySQL.contains("@") == true)
				bySQL = BP.WF.Glo.DealExp(bySQL, en, null);
			DataTable dt = DBAccess.RunSQLReturnTable(bySQL);
			for(DataRow dr : dt.Rows)
			{
				String empName = dr.getValue("Name").toString();
				String empNo = dr.getValue("No").toString();

				// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
				String smsDocReal = smsDoc;
				smsDocReal = smsDocReal.replace("{EmpStr}", empName);
				openUrl = openUrl.replace("{EmpStr}", empNo);
				String paras = "@FK_Flow=" + this.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + this.getFK_Node();

				//发送消息
				BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), openUrl, this.getSMSPushModel(),null,null);

				//处理短消息.
				toEmpIDs += empName + ",";
			}

		}
		// end按照SQL计算

		//begin 发送给指定的接收人
		if (this.getSMSPushWay() == 5)
		{
			if (DataType.IsNullOrEmpty(this.getByEmps()) == true)
			{
				return "发送给指定的人员，则人员集合不能为空。";
			}
			//以逗号分割开
			String[] toEmps = this.getByEmps().split(",");
			for(String empNo : toEmps)
			{
				if (DataType.IsNullOrEmpty(empNo) == true)
					continue;
				BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(empNo);
				// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
				String smsDocReal = smsDoc;
				smsDocReal = smsDocReal.replace("{EmpStr}", emp.getName());
				openUrl = openUrl.replace("{EmpStr}", emp.getNo());
				//发送消息
				BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), openUrl, this.getSMSPushModel(),null,null);
				//处理短消息.
				toEmpIDs += emp.getName() + ",";
			}
		}
		//end 发送给指定的接收人

		//begin 不同的消息事件，接收人不同的处理
		if (this.getSMSPushWay() == 1)
		{
			//工作到达、退回、移交、撤销
			if ((this.getFK_Event().equals(BP.Sys.EventListOfNode.WorkArrive)
					|| this.getFK_Event().equals(BP.Sys.EventListOfNode.ReturnAfter)
					|| this.getFK_Event().equals(BP.Sys.EventListOfNode.ShitAfter)
					|| this.getFK_Event().equals(BP.Sys.EventListOfNode.UndoneAfter))
					&& DataType.IsNullOrEmpty(jumpToEmps) == false)
			{
				/*当前节点的处理人.*/
				toEmpIDs = jumpToEmps;
				String[] emps = toEmpIDs.split(",");
				for(String empNo : emps)
				{
					if (DataType.IsNullOrEmpty(empNo))
						continue;

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					String smsDocReal = smsDoc;
					smsDocReal = smsDocReal.replace("{EmpStr}", empNo);
					openUrl = openUrl.replace("{EmpStr}", empNo);

					BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), openUrl, this.getSMSPushModel(),null,null);
				}
				return "@已向:{" + toEmpIDs + "}发送提醒信息.";
			}
			//end 工作到达、退回、移交、撤销

			//begin 节点发送成功后
			if (this.getFK_Event().equals(BP.Sys.EventListOfNode.SendSuccess) && objs.getVarAcceptersID() != null)
			{
				/*如果向接受人发送消息.*/
				toEmpIDs = objs.getVarAcceptersID();
				String[] emps = toEmpIDs.split(",");
				for(String empNo : emps)
				{
					if (DataType.IsNullOrEmpty(empNo))
						continue;
					if (empNo == WebUser.getNo())
						continue;
					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					String smsDocReal = smsDoc;
					smsDocReal = smsDocReal.replace("{EmpStr}", empNo);
					openUrl = openUrl.replace("{EmpStr}", empNo);
					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;
					BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), openUrl, this.getSMSPushModel(), paras,null);

				}
				return "@已向:{" + toEmpIDs + "}发送提醒信息.";
			}
			//end 节点发送成功后


			//begin 流程结束后、流程删除后
			if (this.getFK_Event().equals(BP.Sys.EventListOfNode.FlowOverAfter)
					|| this.getFK_Event().equals(BP.Sys.EventListOfNode.AfterFlowDel))
			{
				/*向所有参与人发送消息. */
				DataTable dt = DBAccess.RunSQLReturnTable("SELECT Emps,TodoEmps FROM WF_GenerWorkFlow WHERE WorkID=" + workid);
				if (dt.Rows.size() == 0)
					return "";
				String empsStrs = "";
				for(DataRow dr : dt.Rows)
				{
					empsStrs += dr.getValue("Emps");
					String todoEmps = dr.getValue("TodoEmps").toString();
					if (DataType.IsNullOrEmpty(todoEmps) == false)
					{
						String[] strs = todoEmps.split(";");
						todoEmps = "";
						for(String str : strs)
						{
							if (DataType.IsNullOrEmpty(str) == true || empsStrs.contains(str) == true)
								continue;
							empsStrs += str.split(",")[0]+"@";
						}
					}
				}
				String[] emps = empsStrs.split("@");

				for(String empNo : emps)
				{
					if (DataType.IsNullOrEmpty(empNo))
						continue;

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					String smsDoccReal = smsDoc;
					smsDoc = smsDoc.replace("{EmpStr}", empNo);
					openUrl = openUrl.replace("{EmpStr}", empNo);
					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;

					//发送消息
					BP.WF.Dev2Interface.Port_SendMessage(empNo, smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), openUrl, this.getSMSPushModel(), paras,null);
				}
				return "@已向:{" + empsStrs + "}发送提醒信息.";
			}
			//end 流程结束后、流程删除后

			//begin 节点预警、逾期
			if(this.getFK_Event().equals(BP.Sys.EventListOfNode.NodeWarning)
					|| this.getFK_Event().equals(BP.Sys.EventListOfNode.NodeOverDue))
			{
				//获取当前节点的接收人
				GenerWorkFlow gwf = new GenerWorkFlow(workid);
				String[] emps = gwf.getTodoEmps().split(";");
				for(String emp : emps)
				{
					if (DataType.IsNullOrEmpty(emp))
						continue;
					String[] empA = emp.split(",");
					if (DataType.IsNullOrEmpty(empA[0]) ==true || empA[0] == WebUser.getNo())
						continue;
					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					String smsDocReal = smsDoc;
					smsDocReal = smsDocReal.replace("{EmpStr}", empA[0]);
					openUrl = openUrl.replace("{EmpStr}", empA[0]);
					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;
					BP.WF.Dev2Interface.Port_SendMessage(empA[0], smsDoc, mailTitle, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), openUrl, this.getSMSPushModel(), paras,null);
				}
			}
			//end 节点预警、逾期

		}
		//end 不同的消息事件，接收人不同的处理

		//end 消息发送

		return "";

	}

	/** 
	 发送邮件
	 
	 @param title
	 @param openWorkURl
	 @param en
	 @param jumpToEmps
	 @param currNode
	 @param workid
	 @param objs
	 @param r 处理好的变量集合
	 @return 
	 * @throws Exception 
	*/
	private String SendEmail(String title, String openWorkURl, Entity en, String jumpToEmps, Node currNode, long workid, SendReturnObjs objs, Row r) throws Exception
	{
		if (this.getMailPushWay() == 0)
		{
			return "";
		}


			///#region 生成相关的变量？
		String generAlertMessage = ""; //定义要返回的提示消息.
		// 发送邮件.
		String mailTitleTmp = ""; //定义标题.
		String mailDocTmp = ""; //定义内容.

		// 标题.
		mailTitleTmp = this.getMailTitle();
		mailTitleTmp = mailTitleTmp.replace("{Title}", title);
		mailTitleTmp = mailTitleTmp.replace("WebUser.No", WebUser.getNo());
		mailTitleTmp = mailTitleTmp.replace("@WebUser.Name", WebUser.getName());

		// 内容.
		mailDocTmp = this.getMailDoc();
		mailDocTmp = mailDocTmp.replace("{Url}", openWorkURl);
		mailDocTmp = mailDocTmp.replace("{Title}", title);

		mailDocTmp = mailDocTmp.replace("WebUser.No", WebUser.getNo());
		mailDocTmp = mailDocTmp.replace("@WebUser.Name", WebUser.getName());

		//如果仍然有没有替换下来的变量.
		if (mailDocTmp.contains("@"))
		{
			mailDocTmp = BP.WF.Glo.DealExp(mailDocTmp, en, null);
		}


			///#endregion 生成相关的变量？

		//求发送给的人员 ID.
		String toEmpIDs = "";


			///#region 如果发送给指定的节点处理人, 就计算出来直接退回, 任何方式的处理人都是一致的.
		if (this.getMailPushWay() == 3)
		{
			//如果向指定的字段作为发送邮件的对象, 从字段里取数据. 
			String[] nodes = this.getSMSNodes().split("[,]", -1);

			String msg = "";
			for (String nodeID : nodes)
			{
				if (StringHelper.isNullOrEmpty(nodeID) == true)
				{
					continue;
				}

				String sql = "SELECT b.Name, b.Email, b.No FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track a, WF_Emp b WHERE  a.ActionType=1 AND A.WorkID=" + workid + " AND a.NDFrom=" + nodeID + " AND a.EmpFrom=B.No ";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 0)
				{
					continue;
				}

				for (DataRow dr : dt.Rows)
				{
					String emailAddress = dr.getValue("Email").toString();
					String empName = dr.getValue("Name").toString();
					String empNo = dr.getValue("No").toString();

					if (StringHelper.isNullOrEmpty(emailAddress))
					{
						continue;
					}

					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;
					//发送邮件
					BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, this.getFK_Event(), this.getFK_Event() + workid, BP.Web.WebUser.getNo(), null, empNo, paras);
					msg += dr.getValue("Name").toString() + ",";
				}
			}
			return "@已向:{" + msg + "}发送提醒邮件.";

		}

			///#endregion 如果发送给指定的节点处理人, 就计算出来直接退回, 任何方式的处理人都是一致的.


			///#region WorkArrive-工作到达. - 邮件处理.
		if (this.getFK_Event().equals(BP.Sys.EventListOfNode.WorkArrive) || this.getFK_Event().equals(BP.Sys.EventListOfNode.ReturnAfter))
		{
			//工作到达.
			if (this.getMailPushWay() == 1)
			{
				//如果向接受人发送邮件.
				toEmpIDs = jumpToEmps;
				String[] emps = toEmpIDs.split("[,]", -1);
				for (String emp : emps)
				{
					if (StringHelper.isNullOrEmpty(emp))
					{
						continue;
					}

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar = mailDocTmp;
					String mailDocReal = (String)((tempVar instanceof String) ? tempVar : null);
					mailDocReal = mailDocReal.replace("{EmpStr}", emp);

					//获得当前人的邮件.
					BP.WF.Port.WFEmp empEn = new WFEmp(emp);

					//发送邮件.
					BP.WF.Dev2Interface.Port_SendEmail(empEn.getEmail(), mailTitleTmp, mailDocReal, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, emp,null);
				}
				return "@已向:{" + toEmpIDs + "}发送提醒邮件.";
			}

			if (this.getMailPushWay() == 2)
			{
				//如果向指定的字段作为发送邮件的对象, 从字段里取数据. 
				String emailAddress = (String)((r.GetValByKey(this.getMailAddress()) instanceof String) ? r.GetValByKey(this.getMailAddress()) : null);

				//发送邮件
				BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, null,null);
				return "@已向:{" + emailAddress + "}发送提醒邮件.";
			}
		}
			///#region SendSuccess - 发送成功事件. - 邮件处理.
		if (this.getFK_Event().equals(BP.Sys.EventListOfNode.SendSuccess))
		{
			//发送成功事件.
			if (this.getMailPushWay() == 1 && objs.getVarAcceptersID() != null)
			{
				//如果向接受人发送邮件.
				toEmpIDs = objs.getVarAcceptersID();
				String[] emps = toEmpIDs.split("[,]", -1);
				for (String emp : emps)
				{
					if (StringHelper.isNullOrEmpty(emp))
					{
						continue;
					}
					if (emp.equals(WebUser.getNo()))
					{
						continue;
					}

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar2 = mailDocTmp;
					String mailDocReal = (String)((tempVar2 instanceof String) ? tempVar2 : null);
					mailDocReal = mailDocReal.replace("{EmpStr}", emp);

					//获得当前人的邮件.
					BP.WF.Port.WFEmp empEn = new WFEmp(emp);

					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;

					//发送邮件.
					BP.WF.Dev2Interface.Port_SendEmail(empEn.getEmail(), mailTitleTmp, mailDocReal, this.getFK_Event(), "WKAlt" + objs.getVarToNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, emp, paras);
				}
				return "@已向:{" + toEmpIDs + "}发送提醒邮件.";
			}

			if (this.getMailPushWay() == 2)
			{
				//如果向指定的字段作为发送邮件的对象, 从字段里取数据. 
				String emailAddress = (String)((r.GetValByKey(this.getMailAddress()) instanceof String) ? r.GetValByKey(this.getMailAddress()) : null);
				String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;

				//发送邮件
				BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, this.getFK_Event(), "WKAlt" + objs.getVarToNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, null, paras);

				return "@已向:{" + emailAddress + "}发送提醒邮件.";
			}
		}

			///#endregion 发送成功事件.




			///#region SendSuccess - 流程结束. - 邮件处理.
		if (this.getFK_Event().equals(BP.Sys.EventListOfNode.FlowOverAfter))
		{
			//发送成功事件.
			if (this.getMailPushWay() == 1)
			{
				//如果向接受人发送邮件.
				//向所有参与人. 
				String empsStrs = DBAccess.RunSQLReturnStringIsNull("SELECT Emps FROM WF_GenerWorkFlow WHERE WorkID=" + workid, "");
				String[] emps = empsStrs.split("[@]", -1);

				for (String emp : emps)
				{
					if (StringHelper.isNullOrEmpty(emp))
					{
						continue;
					}

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar3 = mailDocTmp;
					String mailDocReal = (String)((tempVar3 instanceof String) ? tempVar3 : null);
					mailDocReal = mailDocReal.replace("{EmpStr}", emp);

					//获得当前人的邮件.
					BP.WF.Port.WFEmp empEn = new WFEmp(emp);

					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;

					//发送邮件.
					BP.WF.Dev2Interface.Port_SendEmail(empEn.getEmail(), mailTitleTmp, mailDocReal, this.getFK_Event(), "FlowOver" + workid, BP.Web.WebUser.getNo(), null, emp, paras);
				}
				return "@已向:{" + toEmpIDs + "}发送提醒邮件.";
			}

			if (this.getMailPushWay() == 2)
			{
				//如果向指定的字段作为发送邮件的对象, 从字段里取数据. 
				String emailAddress = (String)((r.GetValByKey(this.getMailAddress()) instanceof String) ? r.GetValByKey(this.getMailAddress()) : null);

				String paras = "@FK_Flow=" + currNode.getFK_Flow() + "&FK_Node=" + currNode.getNodeID() + "@WorkID=" + workid;

				//发送邮件
				BP.WF.Dev2Interface.Port_SendEmail(emailAddress, mailTitleTmp, mailDocTmp, this.getFK_Event(), "FlowOver" + workid, BP.Web.WebUser.getNo(), null, null, paras);
				return "@已向:{" + emailAddress + "}发送提醒邮件.";
			}
		}

			///#endregion 发送成功事件.

		return generAlertMessage;
	}
	/** 
	 发送短消息.
	 
	 @param title
	 @param openWorkURl
	 @param en
	 @param jumpToEmps
	 @param currNode
	 @param workid
	 @param objs
	 @param r 处理好的变量集合
	 @return 
	 * @throws Exception 
	*/
	private String SendShortMessageToSpecNodes(String title, String openWorkURl, Entity en, String jumpToEmps, Node currNode, long workid, SendReturnObjs objs, Row r) throws Exception
	{
		if (this.getSMSPushWay() == 0)
		{
			return "";
		}

		//定义短信内容.......
		String smsDocTmp = "";


			///#region  生成短信内容
		Object tempVar = this.getSMSDoc();
		smsDocTmp = (String)((tempVar instanceof String) ? tempVar : null);
		smsDocTmp = smsDocTmp.replace("{Title}", title);
		smsDocTmp = smsDocTmp.replace("{Url}", openWorkURl);
		smsDocTmp = smsDocTmp.replace("WebUser.No", WebUser.getNo());
		smsDocTmp = smsDocTmp.replace("@WebUser.Name", WebUser.getName());
		smsDocTmp = smsDocTmp.replace("@WorkID", en.getPKVal().toString());
		smsDocTmp = smsDocTmp.replace("@OID", en.getPKVal().toString());

		//如果仍然有没有替换下来的变量.
		if (smsDocTmp.contains("@") == true)
		{
			smsDocTmp = BP.WF.Glo.DealExp(smsDocTmp, en, null);
		}

		//如果仍然有没有替换下来的变量.
		if (smsDocTmp.contains("@"))
		{
			smsDocTmp = BP.WF.Glo.DealExp(smsDocTmp, en, null);
		}

		//if (smsDocTmp.Contains("@"))
		//    throw new Exception("@短信消息内容配置错误,里面有未替换的变量，请确认参数是否正确:"+smsDocTmp);

		String toEmpIDs = "";

			///#endregion 处理当前的内容.


			///#region 如果发送给指定的节点处理人,就计算出来直接退回,任何方式的处理人都是一致的.
		if (this.getSMSPushWay() == 3)
		{
			 //如果向指定的字段作为发送邮件的对象, 从字段里取数据. 
			String[] nodes = this.getSMSNodes().split("[,]", -1);

			String msg = "";
			for (String nodeID : nodes)
			{
				if (StringHelper.isNullOrEmpty(nodeID) == true)
				{
					continue;
				}

				String sql = "SELECT b.Name, b.Tel ,b.No FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track a, WF_Emp b WHERE  a.ActionType=1 AND A.WorkID=" + workid + " AND a.NDFrom=" + nodeID + " AND a.EmpFrom=B.No ";
				DataTable dt = DBAccess.RunSQLReturnTable(sql);
				if (dt.Rows.size() == 0)
				{
					continue;
				}

				for (DataRow dr : dt.Rows)
				{
					String tel = dr.getValue("Tel").toString();
					String empName = dr.getValue("Name").toString();
					String empNo = dr.getValue("No").toString();

					if (StringHelper.isNullOrEmpty(tel))					
						continue;
					

					// 因为要发给不同的人，所有需要clone 一下，然后替换发送.
					Object tempVar2 = smsDocTmp;
					String mailDocReal = (String)((tempVar2 instanceof String) ? tempVar2 : null);
					mailDocReal = mailDocReal.replace("{EmpStr}", empName);

					String paras = "@FK_Flow=" + this.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + this.getFK_Node();

					//发送邮件.
					BP.WF.Dev2Interface.Port_SendSMS(tel, mailDocReal, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, WebUser.getNo(), null, empNo, paras,null);

					//处理短消息.
					toEmpIDs += empName + ",";
				}
			}
			return "@已向:{" + toEmpIDs + "}发送了短消息提醒.";
		}

			///#endregion 如果发送给指定的节点处理人, 就计算出来直接退回, 任何方式的处理人都是一致的.

		//求发送给的人员ID.

			///#region WorkArrive - 工作到达事件.
		if (this.getFK_Event().equals(BP.Sys.EventListOfNode.WorkArrive) || this.getFK_Event().equals(BP.Sys.EventListOfNode.ReturnAfter))
		{
			//发送成功事件, 退回后事件. 
			if (this.getSMSPushWay() == 1)
			{
				//如果向接受人发送短信.
				toEmpIDs = jumpToEmps;
				String[] emps = toEmpIDs.split("[,]", -1);
				for (String emp : emps)
				{
					if (StringHelper.isNullOrEmpty(emp))
					{
						continue;
					}

					Object tempVar3 = smsDocTmp;
					String smsDocTmpReal = (String)((tempVar3 instanceof String) ? tempVar3 : null);
					smsDocTmpReal = smsDocTmpReal.replace("{EmpStr}", emp);
					BP.WF.Port.WFEmp empEn = new WFEmp(emp);

					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + currNode.getNodeID();

					//发送短信.
					Dev2Interface.Port_SendSMS(empEn.getTel(), smsDocTmpReal, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, emp, null,null);
				}
				return "@已向:{" + toEmpIDs + "}发送提醒手机短信，由 " + this.getFK_Event() + " 发出.";
			}

			if (this.getSMSPushWay() == 2)
			{
				//如果向指定的字段作为发送邮件的对象, 从字段里取数据. 
				String tel = (String)((r.GetValByKey(this.getSMSField()) instanceof String) ? r.GetValByKey(this.getSMSField()) : null);

				//发送短信.
				String paras = "@FK_Flow=" + currNode.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + currNode.getNodeID();
				BP.WF.Dev2Interface.Port_SendSMS(tel, smsDocTmp, this.getFK_Event(), "WKAlt" + currNode.getNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, paras,null,null);
				return "@已向:{" + tel + "}发送提醒手机短信，由 " + this.getFK_Event() + " 发出.";
			}
		}
			///#region SendSuccess - 发送成功事件
		if (this.getFK_Event().equals(BP.Sys.EventListOfNode.SendSuccess))
		{
			//发送成功事件.
			if (this.getSMSPushWay() == 1 && objs.getVarAcceptersID() != null)
			{
				//如果向接受人发送短信.
				toEmpIDs = objs.getVarAcceptersID();
				String[] emps = toEmpIDs.split("[,]", -1);
				for (String empID : emps)
				{
					if (StringHelper.isNullOrEmpty(empID))
					{
						continue;
					}

					Object tempVar4 = smsDocTmp;
					String smsDocTmpReal = (String)((tempVar4 instanceof String) ? tempVar4 : null);
					smsDocTmpReal = smsDocTmpReal.replace("{EmpStr}", empID);

					BP.WF.Port.WFEmp empEn = new WFEmp(empID);

					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + currNode.getNodeID();

					//发送短信.
					Dev2Interface.Port_SendSMS(empEn.getTel(), smsDocTmpReal, this.getFK_Event(), "WKAlt" + objs.getVarToNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, empID, paras,null);
				}
				return "@已向:{" + toEmpIDs + "}发送提醒手机短信.";
			}

			if (this.getSMSPushWay() == 2)
			{
				//如果向指定的字段作为发送短信的发送对象, 从字段里取数据. 
				String tel = (String)((r.GetValByKey(this.getSMSField()) instanceof String) ? r.GetValByKey(this.getSMSField()) : null);
				if (tel != null || tel.length() > 6)
				{

					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + currNode.getNodeID();

					//发送短信.
					BP.WF.Dev2Interface.Port_SendSMS(tel, smsDocTmp, this.getFK_Event(), "WKAlt" + objs.getVarToNodeID() + "_" + workid, BP.Web.WebUser.getNo(), null, paras,null,null);
					return "@已向:{" + tel + "}发送提醒手机短信.";
				}
			}
		}

			///#endregion SendSuccess - 发送成功事件


			///#region FlowOverAfter - 流程结束后的事件
		if (this.getFK_Event().equals(BP.Sys.EventListOfNode.FlowOverAfter))
		{
			//发送成功事件.
			if (this.getSMSPushWay() == 1)
			{
				//向所有参与人. 
				String empsStrs = DBAccess.RunSQLReturnStringIsNull("SELECT Emps FROM WF_GenerWorkFlow WHERE WorkID=" + workid, "");
				String[] emps = empsStrs.split("[@]", -1);
				for (String empID : emps)
				{
					if (StringHelper.isNullOrEmpty(empID))
					{
						continue;
					}

					if (empID.equals(WebUser.getNo()))
					{
						continue;
					}

					Object tempVar5 = smsDocTmp;
					String smsDocTmpReal = (String)((tempVar5 instanceof String) ? tempVar5 : null);
					smsDocTmpReal = smsDocTmpReal.replace("{EmpStr}", empID);

					BP.WF.Port.WFEmp empEn = new WFEmp();
					empEn.setNo(empID);
					empEn.RetrieveFromDBSources();

					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + currNode.getNodeID();

					//发送短信.
					Dev2Interface.Port_SendSMS(empEn.getTel(), smsDocTmpReal, this.getFK_Event(), "FlowOver" + workid, BP.Web.WebUser.getNo(), null, empID, paras,null);
				}
				return "@已向:{" + toEmpIDs + "}发送提醒手机短信.";
			}

			if (this.getSMSPushWay() == 2)
			{
				//如果向指定的字段作为发送短信的发送对象, 从字段里取数据. 
				String tel = (String)((r.GetValByKey(this.getSMSField()) instanceof String) ? r.GetValByKey(this.getSMSField()) : null);
				if (tel != null || tel.length() > 6)
				{
					String paras = "@FK_Flow=" + currNode.getFK_Flow() + "@WorkID=" + workid + "@FK_Node=" + currNode.getNodeID();
					//发送短信.
					BP.WF.Dev2Interface.Port_SendSMS(tel, smsDocTmp, this.getFK_Event(), "FlowOver"+ workid, BP.Web.WebUser.getNo(), null, paras,null,null);
					return "@已向:{" + tel + "}发送提醒手机短信.";
				}
			}
		}

			///#endregion SendSuccess - 发送成功事件

		return "";
	}

	/** 
	 发送短信到其它节点的处理人.
	 
	*/
	private void SendShortMessageToSpecNodeWorks()
	{

	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		String sql = "UPDATE WF_PushMsg SET FK_Flow=(SELECT FK_Flow FROM WF_Node WHERE NodeID= WF_PushMsg.FK_Node)";
		BP.DA.DBAccess.RunSQL(sql);

		return super.beforeUpdateInsertAction();
	}
}