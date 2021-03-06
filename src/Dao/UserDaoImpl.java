package Dao;


import Domain.Block;
import Domain.ProblemBlock;
import Domain.User;
import Utils.XmlUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.text.SimpleDateFormat;

public class UserDaoImpl{
	
	/* (non-Javadoc)
	 * @see cn.itcast.dao.impl.UserDao#add(cn.itcast.domain.User)
	 */

	public void add(User user) {
		try {
			Document document = XmlUtils.getDocument();
			Element root = document.getRootElement();
			
			Element user_tag = root.addElement("user");
			user_tag.setAttributeValue("id", user.getId());
			user_tag.setAttributeValue("username", user.getUsername());
			user_tag.setAttributeValue("password", user.getPassword());
			user_tag.setAttributeValue("email", user.getEmail());
			user_tag.setAttributeValue("birthday", user.getBirthday()==null?"":user.getBirthday().toLocaleString());
			user_tag.setAttributeValue("nikename", user.getNikename());
			
			XmlUtils.write2UsersXml(document);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addNewBolck(Block block, int nowid){
		try {
			Document document = XmlUtils.getBlocksDocument();
			Element root = document.getRootElement();

			Element user_tag = root.addElement("block");
			user_tag.setAttributeValue("id", block.getId().toString());
			user_tag.setAttributeValue("userId", block.getUserId());
			user_tag.setAttributeValue("information1", block.getInformation1());
			user_tag.setAttributeValue("information2", block.getInformation2());

			Element e = (Element) root.selectSingleNode("nowid");
			Attribute attr = e.attribute("id");
			attr.setValue(Integer.toString(nowid));

			XmlUtils.write2BlocksXml(document);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int blockHash(int id){
		return id;
	}

	public boolean isHashRight(int id,String userId){
		try {
			Document document = XmlUtils.getBlocksDocument();
			Element root = document.getRootElement();
			Element e = (Element) root.selectSingleNode("nowid");
			/*String text=root.elementText("nowid");
			System.out.println(text);*/
			Attribute attr = e.attribute("id");
			int nowid = Integer.parseInt(attr.getValue());

			if(blockHash(nowid) == id){

				Block b = new Block();
				b.setId(id);
				b.setUserId(userId);
				addNewBolck(b,id);

				return true;
			}else{
				return false;
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see cn.itcast.dao.impl.UserDao#find(java.lang.String, java.lang.String)
	 */

	public User find(String username,String password) {
		try {
			Document document = XmlUtils.getDocument();
			Element e = (Element) document.selectSingleNode("//user[@username='"+username+"' and @password='"+password+"']");

			if(e==null) {
				return null;
			}
			User user = new User();
			String date = e.attributeValue("birthday");
			if(date==null || date.equals("")) {
				user.setBirthday(null);
			}else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				user.setBirthday(df.parse(date));
			}

				user.setEmail(e.attributeValue("email"));
				user.setId(e.attributeValue("id"));
				user.setNikename(e.attributeValue("nikename"));
				user.setPassword(e.attributeValue("password"));
				user.setUsername(e.attributeValue("username"));
				
				return user;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//����ע����û��Ƿ������ݿ��д���
	/* (non-Javadoc)
	 * @see cn.itcast.dao.impl.UserDao#find(java.lang.String)
	 */
	public boolean find(String username) {
		try {
			Document document = XmlUtils.getDocument();
			Element e = (Element) document.selectSingleNode("//user[@username='"+username+"']");
			if(e==null) {
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//查找一个子问题的结果是否已经被计算出来
	public Boolean findResIndex(String index) {
		try {
			Document document = XmlUtils.getResBlocksDocument();
			Element e = (Element) document.selectSingleNode("//res[@index='"+index+"']");
			if(e==null) {
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//查找配置文件中的用户IP
	public static String getUserIp() {
		try {
			Document document = XmlUtils.getLocalDocument();
			Element e = (Element) document.selectSingleNode("//local[@attr='localip']");
			if(e==null) {
				return null;
			}
			return e.attributeValue("id");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//查找配置文件中的用户ID
	public static String getUserId() {
		try {
			Document document = XmlUtils.getLocalDocument();
			Element e = (Element) document.selectSingleNode("//local[@attr='uid']");
			if(e==null) {
				return null;
			}
			return e.attributeValue("id");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//增加一个新的子问题结果区块
	public void addNewResBolck(ProblemBlock block, int nowid){
		try {
			Document document = XmlUtils.getResBlocksDocument();
			Element root = document.getRootElement();

			int preHash = block.getHash() - 1;
			Element user_tag = root.addElement("res");
			user_tag.setAttributeValue("hash", block.getHash().toString());
			user_tag.setAttributeValue("prehash", Integer.toString(preHash));
			user_tag.setAttributeValue("index", block.getIndex());
			user_tag.setAttributeValue("ip", block.getIp());
			user_tag.setAttributeValue("qid", block.getQid());
			user_tag.setAttributeValue("mid", block.getMid());
			user_tag.setAttributeValue("host", block.getHost());
			user_tag.setAttributeValue("time", block.getTime().toString());
			user_tag.setAttributeValue("res", block.getRes());

			Element e = (Element) root.selectSingleNode("nowid");
			Attribute attr = e.attribute("id");
			attr.setValue(Integer.toString(nowid));

			XmlUtils.write2ResXml(document);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    public static void addNewTestBolck(String host,String ip,String mid,String index){
        try {
            Document document = XmlUtils.getTestBlocksDocument();
            Element root = document.getRootElement();

            Element user_tag = root.addElement("test");
            user_tag.setAttributeValue("host",host);
            user_tag.setAttributeValue("ip",ip);
            user_tag.setAttributeValue("mid",mid);
            user_tag.setAttributeValue("index",index);

            XmlUtils.write2TestXml(document);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获得某一个序号的区块的全部信息并发送
	public ProblemBlock getIndexBlock(int hash) {
		try {
			Document document = XmlUtils.getResBlocksDocument();
			Element e = (Element) document.selectSingleNode("//res[@hash='"+Integer.toString(hash)+"']");

			if(e==null) {
				return null;
			}
			ProblemBlock pb = new ProblemBlock();

			pb.setHash(Integer.parseInt(e.attributeValue("hash")));
			pb.setHost(e.attributeValue("host"));
			pb.setIndex(e.attributeValue("index"));
			pb.setIp(e.attributeValue("ip"));
			pb.setMid(e.attributeValue("mid"));
			pb.setQid(e.attributeValue("qid"));
			pb.setRes(e.attributeValue("res"));

			return pb;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/*public void editAttribute(Element root,String nodeName){

		//获取指定名字的节点，无此节点的会报NullPointerException,时间问题不做此情况的判断与处理了
		Element node=root.element("user");
		Attribute attr=node.attribute("editor");//获取此节点指定的属性,无此节点的会报NullPointerException
		node.remove(attr);//删除此属性

		Attribute attrDate=node.attribute("date");//获取此节点的指定属性
		attrDate.setValue("更改");//更改此属性值

		node.addAttribute("add","增加");//添加的属性
	}*/

	public void addNewUser(User u){
		try {
			Document document = XmlUtils.getBlocksDocument();
			Element root = document.getRootElement();
			Element e = (Element) root.selectSingleNode("nowid");
			Attribute attr = e.attribute("id");
			int nowId = Integer.parseInt(attr.getValue()) + 1;

			Element user_tag = root.addElement("user");

			user_tag.setAttributeValue("id",Integer.toString(nowId));
			user_tag.setAttributeValue("preId",Integer.toString(nowId - 1));
			user_tag.setAttributeValue("ip",u.getIp());
			user_tag.setAttributeValue("username",u.getUsername());
			user_tag.setAttributeValue("password",u.getPassword());
			user_tag.setAttributeValue("type",Integer.toString(u.getType()));
			user_tag.setAttributeValue("coin","500");
			user_tag.setAttributeValue("status","1");
			attr.setValue(Integer.toString(nowId));
			XmlUtils.write2BlocksXml(document);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
