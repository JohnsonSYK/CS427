package edu.ncsu.csc.itrust.server;

import com.google.gson.Gson;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.dao.mysql.ReminderDao;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.exception.DBException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionLogServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private DAOFactory factory;
    private PersonnelDAO personnelDAO;
    private TransactionDAO transactionDAO;
    private static final DateFormat dmyFormatter = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Default Constructor
     */
    public TransactionLogServlet () {
        super();
        factory = DAOFactory.getProductionInstance();
        personnelDAO = factory.getPersonnelDAO();
        transactionDAO = factory.getTransactionDAO();
    }

    /**
     * Dependency Injection Constructor
     * @param factory
     */
    public TransactionLogServlet (DAOFactory factory) {
        super();
        this.factory = factory;
        personnelDAO = factory.getPersonnelDAO();
        transactionDAO = factory.getTransactionDAO();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            String prole = request.getParameter("prole");
            String srole = request.getParameter("srole");
            Optional<Long> transactionNum;
            if(!request.getParameterMap().containsKey("transactionNum")) {
                transactionNum = Optional.empty();
            } else {
                transactionNum = Optional.of(Long.parseLong(request.getParameter("transactionNum")));
            }

            try {
                    Date lower = dmyFormatter.parse(request.getParameter("startDate"));
                    Date upper = dmyFormatter.parse(request.getParameter("endDate"));
                    List<TransactionBean> format = transactionDAO.getFilteredTransactions(prole, srole, lower, upper, transactionNum);
                    String json = new Gson().toJson(format);
                    response.setContentType("application/json");
                    response.getWriter().write(json);
            } catch (ParseException | DBException e) {
                throw new IOException();
            }

    }
}
