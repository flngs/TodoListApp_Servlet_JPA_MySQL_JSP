package com.flngs.todoapp.web;

import com.flngs.todoapp.dao.ITodoDao;
import com.flngs.todoapp.dao.TodoDao;
import com.flngs.todoapp.model.Todo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/")
public class TodoServlet extends HttpServlet {

    private ITodoDao todoDAO;

    @Override
    public void init() {
        todoDAO = new TodoDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        switch (action) {
            case "/new":
                showNewForm(req, resp);
                break;
            case "/insert":
                insertTodo(req, resp);
                break;
            case "/delete":
                deleteTodo(req, resp);
                break;
            case "/edit":
                showEditForm(req, resp);
                break;
            case "/update":
                updateTodo(req, resp);
                break;
            case "/list":
                listTodo(req, resp);
                break;
            default:
                RequestDispatcher dispatcher = req.getRequestDispatcher("login.jsp");
                dispatcher.forward(req, resp);
                break;
        }
    }

        private void listTodo (HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
            List<Todo> listTodo = todoDAO.getAllTodos();
            req.setAttribute("listTodo", listTodo);
            req.getRequestDispatcher("todo-list.jsp").forward(req, resp);
        }

        private void showNewForm (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("todo-form.jsp").forward(req, resp);
        }

        private void showEditForm (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            int id = Integer.parseInt(req.getParameter("id"));
            Todo existingTodo = todoDAO.getTodo(id);
            RequestDispatcher dispatcher = req.getRequestDispatcher("todo-form.jsp");
            req.setAttribute("todo", existingTodo);
            dispatcher.forward(req, resp);

        }

        private void insertTodo (HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String title = req.getParameter("title");
            String username = req.getParameter("username");
            String description = req.getParameter("description");
            boolean isDone = Boolean.parseBoolean(req.getParameter("isDone"));
            Todo newTodo = new Todo(title, username, description, LocalDate.now(), isDone);
            todoDAO.addTodo(newTodo);
            resp.sendRedirect("list");
        }

        private void updateTodo (HttpServletRequest req, HttpServletResponse resp) throws IOException {
            long id = Long.parseLong(req.getParameter("id"));

            String title = req.getParameter("title");
            String username = req.getParameter("username");
            String description = req.getParameter("description");
            LocalDate targetDate = LocalDate.parse(req.getParameter("targetDate"));
            boolean isDone = Boolean.parseBoolean(req.getParameter("isDone"));
            Todo updateTodo = new Todo(id, title, username, description, targetDate, isDone);
            todoDAO.updateTodo(updateTodo);
            resp.sendRedirect("list");
        }

        private void deleteTodo (HttpServletRequest req, HttpServletResponse resp) throws IOException {
            long id = Long.parseLong(req.getParameter("id"));
            todoDAO.deleteTodo(id);
            resp.sendRedirect("list");
        }
    }
