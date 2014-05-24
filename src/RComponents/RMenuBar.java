package RComponents;

import javax.swing.*;
import Main.Main;
import RDialogs.ExportDialog;
import RDialogs.ExportToReimportDialog;
import RDialogs.ImportDialog;
import RDialogs.StaffDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;


public class RMenuBar extends JMenuBar
{
    Main main;
    private final JMenu waybillMenu;
    private final UIManager.LookAndFeelInfo[] infos;

    public RMenuBar(final Main main)
    {
        this.main = main;
        waybillMenu = new JMenu(main.getToolBat().getComboBox().getSelectedItem().toString());
        JMenu warehouseMenu = new JMenu("Склад");

        JMenu suppliersMenu = new JMenu("Постачальники");

        JMenu staffMenu = new JMenu("Працівники");
        JMenuItem staffEdit = new JMenuItem("Меню працівників");
        staffEdit.addActionListener(e ->
        {
            new StaffDialog(main);
            System.out.println("sdg");
        });
        staffMenu.add(staffEdit);

        JMenu reportsMenu = new JMenu("Звіти");

        JMenu serviceMenu = new JMenu("Сервіс");
        JMenuItem remeainsImport = new JMenuItem("Імпорт залишків з Excel документа");
        JMenuItem remeainsExportToRevision = new JMenuItem("Експорт залишків в Excel для ревізії");
        JMenuItem remeainsExportToReimport = new JMenuItem("Експорт залишків в Excel для реімпорту");
        remeainsImport.addActionListener(e -> new ImportDialog(main));
        remeainsExportToRevision.addActionListener(e ->
        {
            try
            {
                new ExportDialog(main);
            } catch (SQLException e1) {e1.printStackTrace();}
        });
        remeainsExportToReimport.addActionListener(e ->
        {
            try
            {
                new ExportToReimportDialog(main);
            } catch (SQLException e1)
            {
                e1.printStackTrace();
            }
        });
        serviceMenu.add(remeainsImport);
        serviceMenu.add(remeainsExportToRevision);
        serviceMenu.add(remeainsExportToReimport);

        JMenu styleMenu = new JMenu("Стилі");
        infos = UIManager.getInstalledLookAndFeels();
        for(int i = 0; i<infos.length; i++)
        {
            ArrayList<JMenuItem> styleMenuItem = new ArrayList<>();
            styleMenuItem.add(new JMenuItem(infos[i].getName()));
            final int finalI = i;
            styleMenuItem.get(i).addActionListener(e ->
            {
                try
                {
                    UIManager.setLookAndFeel(infos[finalI].getClassName());
                    SwingUtilities.updateComponentTreeUI(main);
                } catch (Exception e1) {e1.printStackTrace();}
            });
            styleMenu.add(styleMenuItem.get(i));
        }


        add(waybillMenu);
        add(warehouseMenu);
        add(suppliersMenu);
        add(staffMenu);
        add(reportsMenu);
        add(serviceMenu);
        add(styleMenu);
    }

    public JMenu getWaybillMenu()
    {
        return waybillMenu;
    }
}
