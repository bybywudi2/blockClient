package Controller;

import Dao.UserDaoImpl;
import Utils.DownLoadUtils;
import Utils.XmlUtils;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
/*
Servlet收到qid,index,mid,host之后，调用doCalculate方法计算出结果，回传给分发者的BServer
 */
@WebServlet("/CalculateServlet")
public class CalculateServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String qid = request.getParameter("qid");
        String index = request.getParameter("index");
        int problemIndex = Integer.parseInt(index);
        int res = doCalculate(problemIndex,qid);
        String result = Integer.toString(res);
        String myIp = getRealIp();
        String uid = UserDaoImpl.getUserId();

        String mid = request.getParameter("mid");
        String host = request.getParameter("host");
        UserDaoImpl.addNewTestBolck(host,myIp,mid,index);
        HttpURLConnection connection = null;
        try{
            URL u = new URL("http://"+host+"/blockS/CalculateServlet"+"?result="+result+"&ip="+myIp+"&index="+index+"&qid="+qid+"&mid="+mid+"&host="+host+"&uid="+uid);
            //URL u = new URL("http://39.106.194.129:8080/blockS/CalculateServlet"+"?result="+result+"&ip="+myIp+"&index="+index+"&qid="+qid+"&mid="+mid+"&host="+host);
            connection = (HttpURLConnection)u.openConnection();
            connection.setConnectTimeout(2000);
            //connection.setReadTimeout(2000);

            connection.setRequestMethod("GET");
            System.out.println(connection.getResponseCode());
            //System.out.println(u.toString());
        }catch(MalformedURLException e){
            //e.printStackTrace();
        }catch(IOException e){
            //e.printStackTrace();
        }finally{
            if(connection != null){
                connection.disconnect();
            }
            return;
        }
    }

    public static int doCalculate(int index,String qid){
        if(qid.equals("1")){
            int res = 0;
            for(int i=10000 * (index-1) + 1;i<=10000 * index;i++){
                for(int j=2;j<i;j++){
                    if(i%j == 0){
                        break;
                    }
                    if(j == i-1){
                        res++;
                    }
                }
            }

            return res;
        }else{
            int res = 0;
            for(int i=10000 * (index-1) + 1;i<=10000 * index;i++){
                if(i%2==0 && i%3==0 && i%5==0){
                    res++;
                }
            }

            return res;
        }

    }


    public static String getRealIp(){
        return UserDaoImpl.getUserIp();
       //return "39.106.194.129";
       // return "47.95.194.16";
        //return "39.107.83.2";
    }

    /**
     * 获取服务器IP地址
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String  getServerIp(){
        String SERVER_IP = null;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    SERVER_IP = ip.getHostAddress();
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return SERVER_IP;
    }
}

