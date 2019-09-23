import java.util.ArrayList;


import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * B o a r d W i n d o w M a n a g e r
 *
 * <p>Manage the events of the board window and the communication with the
 * actual board by using a Model-View-Presenter structure.</p>
 * 
 * @author Eric Pietroupo
 * ( ericp@lariennalibrary.com )
 *
 * @version march 26th, 2017
 * ( GNU General Public License )
 *
 *  
 */

public class BoardWindowManager
{
   /** Reference on the window to update the view */
   private BoardWindow p_window;
   /** Reference on the playing board */
   private static Board p_board;
   /** Hold a copy of the currently active player */
   private char p_active_player = '@';
   /** Keep a list of moves in memory because the list is generated at the end
    * of the event listener for the next player. This is why a copy must be 
    * saved.  */
   private ArrayList<Move> p_movelist = null;
   /** Remembers if the games requires an AI or not. Defined by setup */
   private boolean p_2players = true;
   /** Hold the number of moves ahead the AI will used. Defined by setup */
   private int p_nb_moves_ahead = 1;
   
   
   /**
    * Constructor that requires the view on the board window
    * 
    * @param win View of the board window
    */
   public BoardWindowManager ( BoardWindow win)
   {
      p_window = win;
      p_board = new Board();
      
   }
   
   /**
    * This method will pass the layout of the board into it's internal copy
    * of the real game board, and then modify the Board Window with the new
    * content of the board. 
    * 
    * @param board
    * @param jumps_allowed
    */
   public void setup_board ( char [] [] board , boolean jumps_allowed )
   {
      p_board.setup( board );
      Board.set_jumps_allowed( jumps_allowed );
      
      
      
      for ( int j = 0 ; j < Board.SIZE ; j++)
      {
         for ( int i = 0 ; i < Board.SIZE ; i++)
         {
            p_window.set_space(p_board.get_space ( i, j), i, j);
         }
      }
      
      //remove empty spaces
      
      for ( int j = 0 ; j < Board.SIZE ; j++)
      {
         for ( int i = 0 ; i < Board.SIZE ; i++)
         {
            if ( p_board.get_space( i, j) == Board.NONPLAYABLE )
            {
               p_window.remove_space(i, j);
            }
         }
      }
   }
   
   /**
    * Called when the user click on one of the board space. It copies the 
    * coordinates of the button into the command text box.
    * @param x position of the board space
    * @param y position of the board space
    */
   
   public void space_button_click ( int x, int y )
   {
      p_window.append_command(Integer.toString(x+1) + Move.digit_to_letter(y) + " ");
   }

   /**
    * It will take the content of the command text box and interpret it as a 
    * move like when inserted on the command line. If the format or the move
    * is invalid, it will display error dialogs
    * 
    * @param command The text field containing the command.
    */
   public void play_move ( JTextField command )
   {
      Move player_move;
     
      boolean player_switched = false;
      
      //the movelist is null if it's the start of the game, else it would have been already
      //filled up at the end of play_move(). This is essential to check end game condition
      //before exiting the button even.
      if ( p_movelist == null ) {
         p_movelist = p_board.find_valid_moves(p_active_player);
      }      
      player_move = validate_command ( p_active_player, command );
      if ( player_move != null)
      {   
         if ( p_movelist.contains(player_move) == true)
         {
         
            p_board.play (player_move, p_active_player); 
            refresh_board ();
            p_window.set_move_color (player_move);
            
            p_active_player = Board.switch_player ( p_active_player );
            player_switched = true;
            p_window.set_active_player(p_active_player);
            p_movelist = p_board.find_valid_moves(p_active_player);
         }
         else // move is invalid
         {  
            JOptionPane.showMessageDialog(p_window, "Illegal Movement : " + player_move ); 
            
         }
      }
      else
      {
         JOptionPane.showMessageDialog(p_window, "Syntax Error: Input format is 'b2' OR '3c'for adding pieces \nand 'b2 3c' OR '2b c3' for moving pieces ");
         
      }
      p_window.set_command("");
      
      
      
      if ( p_movelist.isEmpty() == true)
      //if the next player has no moves then the game is over.
      {
         end_game();         
      }
         
      // Check if the player has changed and it's a 1 player game, so the AI must play    
      if ( player_switched == true && p_2players == false ) 
      { 
         p_window.set_active_player(p_active_player);
         p_window.set_thinking_ai ( true );
         
         // Java Swing cannot repaint it's components inside an Action Listener
         // Calling the repaint methods simply queue the task. So in order to 
         // change the window while the AI is thinking, the AI code must be 
         // placed in a separate thread. So that this method can exit and 
         // repaint the components.
         
         new Thread() {
            public void run() {
               
               ArtificialIntelligence.negamax ( p_board, p_nb_moves_ahead,  p_nb_moves_ahead, p_active_player );
               
               //  System.out.println ("DEBUG: the best move is " + ArtificialIntelligence.get_bestmove() );
               if ( ArtificialIntelligence.get_bestmove() != null )
               {
                  p_board.play(ArtificialIntelligence.get_bestmove(), p_active_player);
                  p_window.set_move_color(ArtificialIntelligence.get_bestmove());
               }
               // else do nothing since we don't know what move to play. Not likely to happen, but
               // avoids a null pointer exception.
               
               // Update the window and switch back player
               p_active_player = Board.switch_player ( p_active_player );
               refresh_board();
               p_window.set_active_player(p_active_player);
               p_window.set_thinking_ai (false);
               
               
               //Get the move list of the next player
               p_movelist = p_board.find_valid_moves(p_active_player);
               
               if ( p_movelist.isEmpty() == true)
               //if the next player has no moves then the game is over.
               {
                  end_game();         
               }      
                
               p_window.set_command_focus();
            }
         }.start();
         

         
      }  
      else
      {
         p_window.set_command_focus();
      }
   }
   
   /**
    * Setter that modifies the number of players
    * @param value
    */
   
   public void set_2players ( boolean value )
   {
      p_2players = value;
   }
   
   /**
    * Setter that modified the nb of move ahead the AI should think.
    * @param value
    */
   
   public void set_nb_moves_ahead ( int value )
   {
      p_nb_moves_ahead = value;
       
   }
   
   // Private Methods
   
   /**
    * Validate each coordinate expressed as a pair of characters to make sure it is valid.
    * 
    * @param token Coordinates to validate
    * @return true if the token meets the validation requirements.
    */
   
   private static boolean validate_token ( String token )
   {
       boolean valid = false;
       
       // check if composition Letter-Digit or Digit-letter is correct
       if ( token.length() == 2 )
       {
           if ( Character.isLetter ( token.charAt(0) ) ) 
           {
               if ( Character.isDigit ( token.charAt(1) ) )
                  valid = true;
           }
           else if ( Character.isDigit ( token.charAt(0) ) )
           {
               if ( Character.isLetter ( token.charAt(1) ) )
                  valid = true;
           }
       }
       
       return valid;
   }
   
   /**
    * Create a moves using the array of validated tokens passed in parameters.
    * 1 or 2 tokens can be passed according to the type of move.
    * 
    * @param input_tokens An array of tokens previously validated used to create the move
    * @return The created move with the right information
    */
   
   private static Move create_move ( String [] input_tokens )
   {
       Move tmpmove = null;
       //hold the source and destination for each X and Y
       int [] x = new int[2];
       int [] y = new int[2];
       
       
       //put the information in the right position of the array according if it's a character or a digit
       //characters are always the Y coordinates
       for ( int j = 0 ; j < input_tokens.length ; j++ )
          for ( int i = 0 ; i < 2 ; i++ )
          {
             if ( Character.isLetter ( input_tokens [ j ] . charAt ( i ) ) )
             {
                 y [ j ] = Move.letter_to_digit ( input_tokens [ j ] . charAt ( i ) );
             }
             else
             {
                 x [ j ] = Character.getNumericValue ( input_tokens [ j ] . charAt ( i ) ) - 1;
             }
          }
       
       //Create new moves, the number of tokens determines if you add or move a piece
       if ( input_tokens.length == 2 )
          tmpmove = new Move ( x[0], y[0] , x[1] , y[1] );
       else
          tmpmove = new Move ( x[0], y[0]   );
       
       return tmpmove;
   }
   
   /**
    * Validates if the inserted command is a valid command, but not necessarily
    * a valid move.
    * 
    * @param player the character of the player making the move
    * @param txt_command text field containing the command
    * @return a playable move create from the command string.
    */
   
   private Move validate_command ( char player, JTextField txt_command)
   {
      boolean input_valid = false;
      String input_str;
      String [] input_token;
           
      input_str = txt_command.getText().trim();
      input_token = input_str.split (" ");
           
      //Check if the input is composed of 1 or 2 tokens and validate each token separately
      if ( input_token.length <= 2 )
      {
         input_valid = true;
         for ( int i = 0 ; i < input_token.length ; i++ )
         {
             if ( validate_token ( input_token [ i ] ) == false )
             {
                input_valid = false;
             }   
         }    
      }
                      
      if ( input_valid == false )
      {
          
          return null;          
      }
      else 
      {
         return create_move ( input_token );
      }
   }
   
   
   
   /**
    * Update the status of the board into the widget buttons of the window.
    * 
    */
   
   private void refresh_board( )
   {
      for ( int j = 0 ; j < Board.SIZE ; j++)
      {
         for ( int i = 0 ; i < Board.SIZE ; i++)
         {
            p_window.set_space( p_board.get_space(i, j), i, j);     
         }         
      }
   }
   
   /**
    * This method will show the end game dialog box and close the application
    */
   
   private void end_game ( )
   {
      int white = p_board.get_score(Board.WHITE);
      int black = p_board.get_score(Board.BLACK);
      String winner;
      
      if ( white > black )
         winner =  "White wins!";
      else if ( black > white )
         winner = "Black wins!";
      else   
         winner = "It's a tie!";
      
      JOptionPane.showMessageDialog(p_window,
              "White " + Board.WHITE + ": " + white
            + "\nBlack " + Board.BLACK + ": " + black
            + "\n\n" + winner, "Game Over", JOptionPane.PLAIN_MESSAGE );
      
      //End the game
      System.exit(0);   
   }
}
