package fr.sagitarius.MonPlugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class GUI extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JLabel lblCommandePourLa;
    JButton button;
    JTextField field;
    JCheckBox chckbxActiverWhitelist;
    private JTree tree;
    
    
    public GUI(Player player) {
		
		setTitle("TEST-"+player.getName());
        setSize(250,250);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        setAlwaysOnTop(true);
        setVisible(false);
        		

	
        lblCommandePourLa = new JLabel("Commande pour la console");
        lblCommandePourLa.setBounds(10, 10, 224, 14);
        lblCommandePourLa.setVisible(true);
   

        button = new JButton("OK");
        button.setBounds(149, 81, 75, 25);
        button.addActionListener(new ButtonEvent());
        button.setVisible(true);
   
        chckbxActiverWhitelist = new JCheckBox("Activer Whitelist");
        chckbxActiverWhitelist.setBounds(10, 81, 100, 25);
        chckbxActiverWhitelist.addActionListener(new CheckBoxEvent());
        chckbxActiverWhitelist.setVisible(true);


        field = new JTextField();
        field.setBounds(10, 35, 214, 25);
        field.setVisible(true);
        
        getContentPane().add(button);
        getContentPane().add(lblCommandePourLa);
        getContentPane().add(field);
        getContentPane().add(chckbxActiverWhitelist);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(10, 117, 214, 93);
        getContentPane().add(scrollPane_1);
        
        tree = new JTree();
        tree.setModel(new DefaultTreeModel(
        	new DefaultMutableTreeNode("Equipes") {
        		
				private static final long serialVersionUID = 1L;

				{
        			DefaultMutableTreeNode node_1;
        			node_1 = new DefaultMutableTreeNode("bleu");
        				node_1.add(new DefaultMutableTreeNode("sagitarius"));
        			add(node_1);
        			node_1 = new DefaultMutableTreeNode("rouge");
        				node_1.add(new DefaultMutableTreeNode("kirito34"));
        				node_1.add(new DefaultMutableTreeNode("pigoo"));
        			add(node_1);
        		}
        	}
        ));
        
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
        	@Override
        	public void valueChanged(TreeSelectionEvent e) {
        		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        		Bukkit.broadcastMessage("Selection : "+selectedNode.getUserObject().toString());
        	}
        });
        scrollPane_1.setViewportView(tree);
        
        setVisible(true);
        
	}


	public class ButtonEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), field.getText());
			Bukkit.broadcastMessage("Commande dans la console : "+ field.getText());
		}
   
	}



	public class CheckBoxEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			if(chckbxActiverWhitelist.isSelected()){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
			} else {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");
			}
		}
	}
}
