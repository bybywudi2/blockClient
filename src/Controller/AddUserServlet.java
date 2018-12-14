package Controller;

import Dao.UserDaoImpl;
import Domain.ProblemBlock;
import Domain.User;
import Utils.XmlUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AddUserServlet")
public class AddUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String ip = request.getParameter("ip");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String type = request.getParameter("type");

        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        user.setIp(ip);
        user.setType(Integer.parseInt(type));

        UserDaoImpl dao = new UserDaoImpl();

        dao.addNewUser(user);
        return;
    }
}
