/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.WEB;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.DAO.EmployeeDAO;
import com.Model.Employee;

/**
 *
 * @author Arifah S66428
 */
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import com.DAO.EmployeeDAO;
import com.Model.Employee;

/**
 *
 * @author Arifah S66428
 */

@WebServlet("/")

public class EmployeeServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private EmployeeDAO EmployeeDAO;
    
    public void init() {
        EmployeeDAO = new EmployeeDAO();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action  = request.getServletPath();
        
        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                    
                case "/insert":
                    insertEmployee(request, response);
                    break;
                    
                case "/delete":
                    deleteEmployee(request, response);
                    break;
                    
                case "/edit":
                    showEditForm(request, response);
                    break;
                    
                case "/update":
                    updateEmployee(request, response);
                    break;
                    
                default:
                    listEmployee(request, response);
            }
        }
        catch (SQLException e) {
            throw new ServletException(e);
        }
        
    }
    
    private void listEmployee(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        
        List<Employee> employees = EmployeeDAO.selectAllEmployees();
        request.setAttribute("employees", employees);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("employeeList.jsp");
        requestDispatcher.forward(request, response);
        
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("employeeForm.jsp");
        requestDispatcher.forward(request, response);
        
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Employee employee = EmployeeDAO.selectEmployeeByID(id);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("employeeForm.jsp");
        request.setAttribute("employee", employee);
        requestDispatcher.forward(request, response);
        
    }
    
    private void insertEmployee(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String position = request.getParameter("position");
        
        Employee employee = new Employee(name, email, position);
        EmployeeDAO.insertEmployee(employee);
        response.sendRedirect("list");
        
    }
    
    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String position = request.getParameter("position");
        
        Employee employee = new Employee(id, name, email, position);
        EmployeeDAO.updateEmployee(employee);
        response.sendRedirect("list");
        
    }
    
    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        EmployeeDAO.deleteEmployee(id);
        response.sendRedirect("list");
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}