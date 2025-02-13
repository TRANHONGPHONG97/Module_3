package com.example.securitywebapp.servlet;

    import com.example.securitywebapp.bean.UserAccount;
    import com.example.securitywebapp.utils.AppUtils;
    import com.example.securitywebapp.utils.DataDAO;

    import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

    @WebServlet("/login")
    public class LoginServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        public LoginServlet() {
            super();
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            RequestDispatcher dispatcher //
                    = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");

            dispatcher.forward(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            UserAccount userAccount = DataDAO.findUser(userName, password);

            if (userAccount == null) {
                String errorMessage = "Invalid userName or Password";

                request.setAttribute("errorMessage", errorMessage);

                RequestDispatcher dispatcher //
                        = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");

                dispatcher.forward(request, response);
                return;
            }

            AppUtils.storeLoginedUser(request.getSession(), userAccount);

            //
            int redirectId = -1;
            try {
                redirectId = Integer.parseInt(request.getParameter("redirectId"));
            } catch (Exception e) {
            }
            String requestUri = AppUtils.getRedirectAfterLoginUrl(request.getSession(), redirectId);
            if (requestUri != null) {
                response.sendRedirect(requestUri);
            } else {
                // Mặc định sau khi đăng nhập thành công
                // chuyển hướng về trang /userInfo
                response.sendRedirect(request.getContextPath() + "/userInfo");
            }

        }
}
