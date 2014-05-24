package RDialogs;

import Main.Main;
import Support.DBI;
import Support.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliersDialog extends RDialog
{
    private Main main;
    private final JList<String> list;
    private DBI suppliersDB;
    private final DefaultListModel<String> listModel = new DefaultListModel<String>();
    private final JTextField searchField;
    private final JScrollPane scrollPane;

    public SuppliersDialog(final Main main)
    {
        super(main, "Постачальники", true);
        this.main = main;

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        ResultSet resultSet;
        suppliersDB = new DBI("databassesabc");
        try
        {
            resultSet = suppliersDB.getSt().executeQuery("SELECT * FROM `постачальники`");
            String s;
            while (resultSet.next())
            {
                s = resultSet.getString(1);
                listModel.addElement(s);
            }
        } catch (SQLException e) {e.printStackTrace();}

        searchField = new JTextField(20);
        list = new JList<>(listModel);
        JButton newSupplierBtn = new JButton("Новий");
        JButton deleteSupplierBtn = new JButton("Видалити");
        JButton editSupplierBtn = new JButton("Редактувати");

        newSupplierBtn.addActionListener(new newSupplierAction());
        deleteSupplierBtn.addActionListener(new deleteSupplierAction());
        editSupplierBtn.addActionListener(new editSupplierAction());
        scrollPane = new JScrollPane(list);

        add(searchField, new GBC(0, 0, 3, 1).setAnchor(GBC.WEST));
        add(scrollPane, new GBC(0, 1, 3, 1).setAnchor(GBC.WEST).setIpad(125, 100));
        add(newSupplierBtn, new GBC(0, 2).setAnchor(GBC.WEST));
        add(deleteSupplierBtn, new GBC(1, 2).setAnchor(GBC.CENTER));
        add(editSupplierBtn, new GBC(2, 2).setAnchor(GBC.EAST));

        pack();

        setResizable(false);
        setLocationRelativeTo(main);
        setLocation(main.getX() + main.getWidth()/2 - this.getWidth()/2, main.getY() + main.getHeight()/2 - this.getHeight()/2);
        revalidate();
        setVisible(true);
        suppliersDB.close(main.k);
    }

    public SuppliersDialog getThisFrame()
    {
        return this;
    }

    private class newSupplierAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            suppliersDB = new DBI("databassesabc");
            String name = JOptionPane.showInputDialog(getThisFrame(), "Введіть ім'я постачальника:");
            try
            {
                suppliersDB.getSt().execute("INSERT INTO `постачальники` (`Постачальник`) VALUE ('" + name + "');");
            } catch (SQLException e1) {e1.printStackTrace();}
            listModel.addElement(name);
            int index = listModel.size() - 1;
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
            suppliersDB.close(main.k);
            scrollPane.revalidate();
        }
    }

    private class deleteSupplierAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(list.getSelectedIndex() != -1)
            {
                suppliersDB = new DBI("databassesabc");
                try
                {
                    suppliersDB.getSt().execute("DELETE FROM `постачальники` WHERE `Постачальник`='" + listModel.getElementAt(list.getSelectedIndex()) + "';");
                } catch (SQLException e1) {e1.printStackTrace();}
                listModel.remove(list.getSelectedIndex());
                suppliersDB.close(main.k);
            }
        }
    }

    private class editSupplierAction implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            suppliersDB = new DBI("databassesabc");
            int i = list.getSelectedIndex();
            String start = listModel.getElementAt(i);
            String end = JOptionPane.showInputDialog(main, "", start);
            try {
                suppliersDB.getSt().execute("UPDATE `постачальники` SET `Постачальник`='" + end + "' WHERE `Постачальник`='" + start + "';");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            listModel.setElementAt(end, i);
        }
    }
}
