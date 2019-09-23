import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * B o a r d W i n d o w
 *
 * <p>Class that holds the widget to draw the board in a window.
 * The positioning is done without a layout manager.</p>
 * 
 * @author Eric Pietroupo
 * ( ericp@lariennalibrary.com )
 *
 * @version march 26th, 2017
 * ( GNU General Public License )
 *
 *  
 */
public class BoardWindow extends JFrame
{
   /** Generated UID, required by JFrame */
   private static final long serialVersionUID = 4579127847176658760L;
   /** Color of the "Black" pieces which will look like Dark Blue */
   public static Color BLACK = new Color ( 0, 0, 150); //looks like dark blue
   /** Color of the "White" pieces which will look like Red Brown */
   public static Color WHITE = new Color ( 150, 0, 0); //looks like red brown
   /** Width of the window */
   public static int WIN_WIDTH = 340;
   /** Height of the window */
   public static int WIN_HEIGHT = 420;

   /** Each board space is a button */
   private JButton [][] p_btn_space = new JButton[Board.SIZE][Board.SIZE];
   /** Coordinates of the top row */
   private JLabel [] p_lbl_topcoordinate = new JLabel [Board.SIZE];
   /** Coordinates of the side row */
   private JLabel [] p_lbl_sidecoordinate = new JLabel [Board.SIZE];
   /** Allows command to be written manually or to be inserted with buttons */
   private JTextField p_txt_command = new JTextField ("");
   /** Play move button */
   private JButton p_btn_play = new JButton ("Play Move");
   /** Thinking label shown when AI is playing */
   private JLabel p_lbl_thinking = new JLabel ("Thinking..."); 
   
   /** Font used to draw the board pieces */
   private Font p_fnt_board = new Font ( "Arial", Font.BOLD, 30);
   /** Font used for the command text field*/
   private Font p_fnt_command = new Font ( "Courier", Font.BOLD, 20 );
   /** Identifies the current active player */
   private JLabel p_lbl_active_player = new JLabel ("@");
   
   /** A static window manager because we assume there is only 1 board window in the program */
   private static BoardWindowManager ps_event_manager;
   
   /** A special ActionListener to be added to each board space. 
    * The y and Y position is passed in parameter to the listener
    * so that they can be used in the actionPerformed method.  */
   public static class BoardSpaceListener implements ActionListener
   {
      /** x position of the button on the board */
      private int p_x;
      /** y position of the button on the board */
      private int p_y;
      
      /** Constructor that requires the position on the board
       * 
       * @param x position of the space
       * @param y position of the space
       * */
      public BoardSpaceListener ( int x, int y )
      {
         p_x = x;
         p_y = y;
      }
      
      /** 
       * @param event that triggered the listener
       */
      @Override
      public void actionPerformed(ActionEvent event)
      {
         ps_event_manager.space_button_click(p_x, p_y);         
      }
      
   }
   
   /** Position and redefine the widgets in the window */  
   public void build_interface ( )
   {
      // gets the screen size to adjust the position of the window      
      Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
      
      setTitle ( "Ataxx / Virus");
      setLayout(null);
      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      setBounds( (screen_size.width / 2) - ( WIN_WIDTH / 2), 
            (screen_size.height / 2) - (WIN_HEIGHT / 2), WIN_WIDTH, WIN_HEIGHT);
      
      
      
      for ( int j = 0 ; j < Board.SIZE ; j++) 
      {
         //Create, position and add the side labels for the coordinates
         p_lbl_sidecoordinate [j] = new JLabel ( Character.toString( (char) (j + 'a')));
         p_lbl_sidecoordinate [j].setBounds( 5, (40 * j) + 40 , 25, 25);
         p_lbl_sidecoordinate [j].setFont ( p_fnt_command );
         add ( p_lbl_sidecoordinate [ j ]);
         
         for ( int i = 0 ; i < Board.SIZE ; i++)
         {
            if ( j == 0) 
            {
               //Create, position and add the top labels for the coordinates
               p_lbl_topcoordinate [i] = new JLabel ( String.format("%d", i + 1));
               p_lbl_topcoordinate [i].setBounds( ( 40 * i) + 40, 5 , 25, 25 );
               p_lbl_topcoordinate [i].setFont ( p_fnt_command );
               add ( p_lbl_topcoordinate [ i ]);
               
            }
            
            // Create, position and add buttons to create the board.
            p_btn_space [ j ] [ i ] = new JButton ();
                           
            p_btn_space [ j ] [ i ] . setFont(p_fnt_board);
            p_btn_space [ j ] [ i ] . setBackground ( Color.WHITE );
            
            p_btn_space [ j ] [ i ] . setMargin(new Insets ( 0, 0, 0, 0));
            p_btn_space [ j ] [ i ] . setBounds ( ( 40 * i) + 30, (40 * j) + 30, 40, 40);
            p_btn_space [ j ] [ i ] .addActionListener ( new BoardSpaceListener ( i, j) );
            
            add ( p_btn_space [ j ] [ i ]);
         }
      }
      
      //Position the bottom widgets
      p_txt_command.setBounds(40, 320, 140, 40 );
      p_txt_command.setFont(p_fnt_command);
      p_lbl_thinking.setBounds(40, 320, 140, 40 );
      p_lbl_thinking.setFont(p_fnt_command);
      p_lbl_thinking.setVisible(false); // only shown when AI is thinking
      p_btn_play.setBounds( 190, 320, 120, 40 );
      p_lbl_active_player.setBounds(5, 320, 40, 40);
      p_lbl_active_player.setFont( p_fnt_board );
      
      // Add action listeners
      ActionListener tmplistener =  new ActionListener () 
      {

         @Override
         public void actionPerformed(ActionEvent arg0)
         {
            ps_event_manager.play_move( p_txt_command);
            
         }
         
      };

      
      p_btn_play.addActionListener ( tmplistener );
      p_txt_command.addActionListener( tmplistener );
      
      
      // Add widgets to the frame
      add( p_lbl_active_player);
      add( p_txt_command );
      add ( p_lbl_thinking );
      add(p_btn_play);
      
      getContentPane().setBackground( new Color ( 175, 175, 175));
      setVisible(true);
      
      p_txt_command.requestFocusInWindow();
      
   }
   
   
   /**
    * Set the object that will manage the events. and MVP model is used for the 
    * event management.
    *    
    * @param manager
    */
   public void set_event_manager ( BoardWindowManager manager )
   {
      ps_event_manager = manager;
   }
   
   /**
    * Change the content of the command text field
    * 
    * @param value to add to the text field
    */
   
   public void set_command ( String value ) 
   {
      p_txt_command.setText(value);
   }
   
   /**
    * Set the focus on the command text field
    */
   
   public void set_command_focus ( )
   {
      p_txt_command.requestFocusInWindow();
   }
   
   /**
    * Change and disable the text field when the AI is thinking
    */
   
   public void set_thinking_ai ( boolean value )
   {
      if ( value == true )
      {
         p_txt_command.setVisible(false);
         p_lbl_thinking.setVisible(true );
         p_btn_play.setEnabled(false);
      }
      else 
      {
         p_txt_command.setVisible(true);
         p_lbl_thinking.setVisible(false);         
         p_btn_play.setEnabled(true);
      }
   }
   
   /**
    * This method colorise the space of the board where the AI
    * placed his piece and or where he moved it from.
    * 
    * @param move that was played
    */
   
   public void set_move_color ( Move move )
   {
      //re-white everything
      for ( int j = 0 ; j < p_btn_space.length; j++)
      {
         for ( int i = 0 ; i < p_btn_space[j].length; i++)
         {
            p_btn_space [j][i].setBackground ( Color.WHITE);
         }
      }
      
      p_btn_space [ move.get_y() ] [ move.get_x()] .setBackground ( Color.CYAN );
      
      if ( move.is_add_piece() == false)
      {
         p_btn_space [ move.get_destination_y() ] [ move.get_destination_x()] .setBackground ( Color.CYAN );
      }
   }
   
   /**
    * Append the string passed in parameter to the content of the command 
    * text field.
    * 
    * @param value to append to the text field.
    */
   
   public void append_command ( String value )
   {
      p_txt_command.setText( p_txt_command.getText() + value );
   }
   
   /**
    * Change the content of the board displayed on the screen. It changes the 
    * text at the top of the buttons.
    * 
    * @param value Character to display
    * @param x position of the button to change
    * @param y position of the button to change
    */
   
   public void set_space (char value, int x, int y )
   {
      p_btn_space [ y ] [ x ] . setText ( Character.toString(value));
      
      switch ( value )
      {
      case Board.BLACK:
         p_btn_space [ y ] [ x ] . setForeground ( BoardWindow.BLACK );
      break;
      case Board.WHITE:
         p_btn_space [ y ] [ x ] . setForeground ( BoardWindow.WHITE );
      break;
      default:
         p_btn_space [ y ] [ x ] . setForeground ( new Color ( 0, 0, 0) );
      break;   
      }
   }
   
   /**
    * Remove widgets from the board to create unplayable areas.
    * 
    * @param x position of the widget
    * @param y position of the widget
    */
   
   public void remove_space ( int x, int y )
   {      
      //I decided to hide the space instead of removing it. In case a bug tries 
      //to modify the content of the space, it won't create a null pointer
      //exception.
      p_btn_space [ y ] [ x ].setVisible(false);
   }
   
   /**
    * Change the label that identifies the color of the active player
    * 
    * @param value char of the active player.
    */
   public void set_active_player ( char value )
   {
      p_lbl_active_player.setText ( Character.toString(value) );
   }
}
