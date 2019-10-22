package com.msgclient;

import com.msgresources.UserInterface;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.*;
/**
 * Shows one or more lines of options.
 * In the constructor it can be set, if the user has the option to select one or more options.
 * Objects that are parsed to this class, must implement UserInterface, they are then listed showing their name through calling getDisplayName() on the object.
 */
public class UserTable extends JPanel
{
    JList<Object> list = null;
    DefaultListModel<Object> model = null;
    UserInterface[] options;

    public UserTable(UserInterface[] options)
    {
        this(options, new Dimension(300, 200));
    }


    public UserTable(UserInterface[] options, Dimension size)
    {
        this(options, size, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }


    public UserTable(UserInterface[] options, Dimension size, int selectionmode)
    {
        this(options, new Dimension(300, 200), selectionmode, BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    }


    public UserTable(UserInterface[] options, Dimension size, int selectionmode, Border border)
    {
        super(new GridLayout(1,0));
        this.options = options;
        this.setBackground(Color.WHITE);
        this.setBorder(border);
        //size.setSize(size.getWidth()-2, size.getHeight()-7);
        model = new DefaultListModel<>();
        if(options != null)
        {
            for(int i = 0; i < options.length; i++)
            {
                model.addElement(options[i]);
            }
        }

        list = new JList<>(model);
        list.setSelectionMode(selectionmode);
        //list.setPreferredSize(size);

        list.setCellRenderer(
                new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (renderer instanceof JLabel && value instanceof UserInterface) {
                            ((JLabel) renderer).setText(((UserInterface) value).getDisplayName());
                        }
                        return renderer;
                    }
                });
        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setSize(size);

        this.add(scrollPane);
        //this.add(list);
    }

    /**
     * Adds a specific mouseadapter to the list
     */
    public void addMouseListener(MouseAdapter mouseadapter)
    {
        list.addMouseListener(mouseadapter);
    }

    /**
     * Adds our generic EO mouse adapter to the list, this will set the data of the list in the EOOperation defined and do a runCommand with the EOOperation. If the user right clicks the cell
     */
    public void addMouseListener(ClientGUIInterface gui)
    {
        MouseAdapter mouseadapter =
                new java.awt.event.MouseAdapter()
                {
                    public void mouseClicked(java.awt.event.MouseEvent e)
                    {
                        if(e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON2) {
                            System.out.println("Click! " + ((UserInterface)list.getModel().getElementAt(list.locationToIndex(e.getPoint()))).getDisplayName());
                            gui.userSelected(((UserInterface)list.getModel().getElementAt(list.locationToIndex(e.getPoint()))));
                            list.clearSelection();
                        }
                    }
                };
        list.addMouseListener(mouseadapter);
    }

    /**
     * Returns selected objects
     */
    public Object[] getSelected()
    {
        java.util.List<Object> r = list.getSelectedValuesList();
        if(r == null)
        {
            return(null);
        }
        else
        {
            return(r.toArray());
        }
    }

    /**
     * Marks the selected options in the list
     */
    public void setList(UserInterface[] options)
    {
        this.options = options;
        setList(options, null);
    }

    public void setList(java.util.List<UserInterface> options)
    {
        UserInterface[] uif = new UserInterface[options.size()];
        for(int i = 0; i < options.size(); i++){
            uif[i] = options.get(i);
        }
        setList(uif);
    }

    /**
     * Get all options
     */
    public UserInterface[] getList()
    {
        return(this.options);
    }

    /**
     * Sets the options and selects the options that are in the selected array.
     */
    public void setList(UserInterface[] options, UserInterface[] selected)
    {
        model = new DefaultListModel<>();
        int selectedindex = 0;
        int[] iselected = null;
        if(selected != null)
        {
            iselected = new int[selected.length];
        }

        if(options != null)
        {
            for(int i = 0; i < options.length; i++)
            {
                for(int j = 0; selected != null && j < selected.length; j++)
                {
                    if(selected[j].equals(options[i]))
                    {
                        iselected[selectedindex] = i;
                        selectedindex++;
                    }
                }
                model.addElement(options[i]);
            }
        }
        list.setModel(model);
        for(int j = 0; selected != null && j < iselected.length; j++)
        {
            list.addSelectionInterval(iselected[j], iselected[j]);
        }
    }
}