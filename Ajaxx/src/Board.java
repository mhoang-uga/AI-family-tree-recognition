import java.util.ArrayList;

/**
 * B o a r d
 *
 * <p>Manage the content of the board</p>
 * 
 * @author Eric Pietrocupo
 * (ericp@lariennalibrary.com)
 * 
 * @version 10 dec 2016
 * ( GNU General Public License )
 * 
 */

public class Board {
   
    // Enumeration
   
    // Hold the board types, a string for identification and a reference on the actual board
    // Makes it easier to dealt with combo box this way.
    public static enum Layout 
    { 
       REGULAR ("Regular", BOARD_7X7 ), 
       CROSS ("Cross", BOARD_7X7_CROSS ), 
       CENTER ("Center", BOARD_7X7_CENTER ), 
       ISLAND ("Island", BOARD_7X7_ISLAND ), 
       RIVER ("River", BOARD_7X7_RIVER ), 
       DEBUG ("Debug", BOARD_7X7_DEBUG ); 
    
       private String p_name;
       private char [][] p_board;
       
       private Layout ( String name, char[][] board)
       {
          p_name = name;
          p_board = board;
       }
       
       @Override
       public String toString() {
           return p_name;
       }
       
       public char[][] get_board()
       {
          return p_board;
       }
       
    };
   
    // Constants
    
    /** Default regular board setup */
    public final static char [][] BOARD_7X7 =  { { '@', ' ', ' ', ' ', ' ', ' ', 'O' },
                                                 { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                 { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                 { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                 { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                 { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                 { 'O', ' ', ' ', ' ', ' ', ' ', '@' } };
                                      
    /** Board setup with a 4 unplayable space cross in the middle */                                             
        
    public final static char [][] BOARD_7X7_CROSS =   { { '@', ' ', ' ', ' ', ' ', ' ', 'O' },
                                                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                        { ' ', ' ', ' ', 'X', ' ', ' ', ' ' },
                                                        { ' ', ' ', 'X', ' ', 'X', ' ', ' ' },
                                                        { ' ', ' ', ' ', 'X', ' ', ' ', ' ' },
                                                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                        { 'O', ' ', ' ', ' ', ' ', ' ', '@' } };
    
    /** Board setup with separated an area in the center */                                                         
    public final static char [][] BOARD_7X7_CENTER =  { { '@', ' ', ' ', ' ', ' ', ' ', 'O' },
                                                        { ' ', ' ', 'X', ' ', 'X', ' ', ' ' },
                                                        { ' ', 'X', ' ', ' ', ' ', 'X', ' ' },
                                                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                        { ' ', 'X', ' ', ' ', ' ', 'X', ' ' },
                                                        { ' ', ' ', 'X', ' ', 'X', ' ', ' ' },
                                                        { 'O', ' ', ' ', ' ', ' ', ' ', '@' } };  
                                   
    /** Board setup with a single space in the middle of the board */                                                    
    public final static char [][] BOARD_7X7_ISLAND =  { { '@', ' ', ' ', ' ', ' ', ' ', 'O' },
                                                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                        { ' ', ' ', 'X', 'X', 'X', ' ', ' ' },
                                                        { ' ', ' ', 'X', ' ', 'X', ' ', ' ' },
                                                        { ' ', ' ', 'X', 'X', 'X', ' ', ' ' },
                                                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                        { 'O', ' ', ' ', ' ', ' ', ' ', '@' } };  
    
    /** Board setup with a single space in the middle of the board */                                                    
    public final static char [][] BOARD_7X7_RIVER =   { { '@', ' ', ' ', ' ', ' ', ' ', 'O' },
                                                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                        { 'X', 'X', 'X', 'X', 'X', 'X', 'X' },
                                                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                                                        { 'O', ' ', ' ', ' ', ' ', ' ', '@' } };       
                                                        
    /** Board with an end game position to simplify end game debugging */                                                     
    public final static char [][] BOARD_7X7_DEBUG =   { { '@', '@', '@', ' ', 'O', 'O', 'O' },
                                                        { '@', '@', '@', ' ', 'O', 'O', 'O' },
                                                        { '@', '@', '@', 'X', 'O', 'O', 'O' },
                                                        { ' ', ' ', 'X', ' ', 'X', ' ', ' ' },
                                                        { 'O', 'O', 'O', 'X', '@', '@', '@' },
                                                        { 'O', 'O', 'O', ' ', '@', '@', '@' },
                                                        { 'O', 'O', 'O', ' ', '@', '@', '@' } };  
                                                        
    /** Indicates the character used by the black player */                                                    
    public final static char BLACK = '@';
    /** Indicates the character used by the white player */
    public final static char WHITE = 'O';
    /** Indicates which character is a playable space */
    public final static char PLAYABLE = ' ';
    /** Indicates it's a non playable space */
    public final static char NONPLAYABLE = 'X';
    public final static int SIZE = 7;
    /** A copy of the board width for easier coding */
    private static final int p_width = SIZE;
    /** A copy of the board height for easier coding */
    private static final int p_height = SIZE;
    /** Since there will be multiple copies of the board, the rule remains the 
     * same for all the boards. */
    private static boolean p_rule_jumps_allowed = false;
    
    // Properties
    
    /** Contains the current status of the board.  */                                                   
    private char [][] p_board;  
    /** keeps track of the number of white pieces. avoids recalculating during
     * the AI algorithm*/
    private int p_nb_white = 0;
    /** keeps track of the number of black pieces. avoids recalculating during
     * the AI algorithm*/
    private int p_nb_black = 0;
    

    
        
    // Constructors
    
    /**
     * Default constructor
     */
    public Board ()
    {
    }
    
    /**
     * Using a copy constructor to duplicate boards.  
     * 
     * 
     */
    
    public Board ( Board copy )
    {
       p_board = new char [ p_height ] [ p_width ];
       
       for ( int j = 0; j < p_board.length ; j++ )
       {
          for ( int i = 0 ; i < p_board [ j ].length; i++)
          {
             p_board [ j ] [ i ] = copy.p_board [ j ] [ i ];
          }
       }
       p_nb_black = copy.p_nb_black;
       p_nb_white = copy.p_nb_white;
       
       
    }
    
    // Instance Methods
    
    /**
     * Setup the board according to the template board passed into parameter.
     * 
     * @param setup_board A template board used to setup the game
     */
    public void setup ( char [][] setup_board )
    {
        //p_width = setup_board[0].length;
        //p_height = setup_board.length;
        p_board = new char [ p_height ] [ p_width ];
        
        //Simply copy an constant array into the main array
        for ( int j = 0 ; j < p_height ; j++ )
        {
            for ( int i = 0 ; i < p_width ; i++ )
            {
                p_board [ j ] [ i ] = setup_board [ j ] [ i ];
            }    
        }
        
        p_nb_white = get_score ( Board.WHITE );
        p_nb_black = get_score ( Board.BLACK);
       // System.out.println ("DEBUG: white= " + p_nb_white + " | black " + p_nb_black );
    }
    
    /**
     * Add up all the space with the pieces of a specific color.
     * 
     * @param color to verify
     * @return Number of pieces of that color on the board.
     */
    public int get_score ( char color )
    {
       int tmpscore = 0;   
    
       for ( int j = 0; j < p_height ; j++ )
       {   
          for ( int i = 0 ; i < p_width ; i++ )
          {
             if ( p_board[ j ][ i ] == color )
             {
                tmpscore++;
             }
          }
          
       }   
       
       return tmpscore;
    }
    
    /**
     * Evaluate the status of the board by calculating the difference of black
     * and white pieces
     * 
     * @param color of the player
     * @return number of pieces it has more than his opponent
     */
    
    public int evaluate ( char color)
    {
       
       if ( color == BLACK)
       {
          return p_nb_black - p_nb_white;
       }
       else
       {
          return p_nb_white - p_nb_black;
       }
       

    }
    
    /**
     * Debugging methods that shows the number of black and white pieces on
     * the console.
     */
    public void show_nb_pieces ()
    {
       System.out.println ( "Black:  " + p_nb_black + "White: " + p_nb_white );
    }
    
    
    /**
     * Get the content of a board space.          
     *     
     * @param x position to check on the board
     * @param y position to check on the board
     * @return a character with the content of the space.
     */
    public char get_space ( int x, int y )
    {
       return p_board [ y ] [ x ];
    }
    
    /**
     * Modify an internal rule to allow or not piece movements. If set to false
     * moving pieces will be an illegal move.
     *  
     * @param value true or false
     */
    
    public static void set_jumps_allowed ( boolean value )
    {
       p_rule_jumps_allowed = value;
    }
    
        
    /**
     * This method build a list of the possible valid moves according to the color of the player passed into 
     * parameters. There is 2 kind of moves, those that add pieces and those that move pieces.
     * 
     * @param player character identifying the color of the player that will be making that move
     * @return a move list with all the valid moves
     * 
     */
    
    public ArrayList<Move> find_valid_moves ( char player)
    {
        ArrayList<Move> tmplist = new ArrayList<Move>();
        boolean is_valid = false;
        Move tmpmove;
        int starty;
        int startx;
        int endy;
        int endx;
       
        
        // Find valid moves for adding pieces
        for ( int j = 0 ; j < p_height ; j++ )
           for ( int i = 0 ; i < p_width; i++ )
              if ( p_board [j] [i] == PLAYABLE ) // check for playable positions
              {
                  // Determine the area to search for a piece. Using min max to make sure
                  // we do not exceed the edge of the array
                  is_valid = false;
                  starty = Math.max ( j - 1, 0 );
                  startx = Math.max ( i - 1 , 0 );
                  endy = Math.min ( j + 1, p_height - 1 );
                  endx = Math.min ( i + 1, p_width - 1);
                        
                  for ( int y = starty ; y <= endy; y++ )
                     for ( int x = startx ; x <= endx; x++ )
                     {
                        if ( p_board [ y ] [ x ] == player )
                        {
                           is_valid = true; // if at least 1 piece is present the move is valid
                           //System.out.println ("move valid: ");
                        }
                        
                     }
                                          
                  if ( is_valid == true )
                  {
                     // Create the move since it's valid
                     tmpmove = new Move ( i, j);
                     tmplist.add ( tmpmove );
                     //System.out.printf ("\nAdding move [%d,%d]: %s", i, j, tmpmove.toString() );
                  }
              }
        
        // Find valid moves for moving pieces
        if ( p_rule_jumps_allowed == true ) // moving pieces becomes illegal moves if false
        {
           for ( int j = 0 ; j < p_height ; j++ )
           {   
              for ( int i = 0 ; i < p_width; i++ )
              {   
                 if ( p_board [j] [i] == player ) // find player pieces
                 {
                     // Create an area to search for a valid place to move to using again min max not to exceed edges   
                     starty = Math.max ( j - 2, 0 );
                     startx = Math.max ( i - 2 , 0 );
                     endy = Math.min ( j + 2, p_height - 1 );
                     endx = Math.min ( i + 2, p_width - 1);
                           
                     for ( int y = starty ; y <= endy; y++ )
                        for ( int x = startx ; x <= endx; x++ )
                        {
                           if ( p_board [ y ] [ x ] == PLAYABLE )
                           {
                              //If the absolute distance between source and destination is 2 for either X or Y
                              //then it means the destination square is 2 space away
                              if ( Math.abs ( j - y ) == 2 || Math.abs ( i - x ) == 2 ) 
                              {
                                  tmpmove = new Move ( i, j, x, y );
                                  tmplist.add ( tmpmove );
                                  //System.out.println ("move valid: ");
                               }    
                           }
                           
                        }
                     
                 }
              }
           }   
        }      
        return tmplist;
    }    
    
    /**
     * Play a move previously validated and propagate color to adjacent pieces.
     * 
     * @param move Move made by the player
     * @param player Color of the player
     */
    public void play ( Move move, char player )
    {
      
        
        if ( move.is_add_piece() == true )
        {
            // Manage piece duplication
            p_board [ move.get_y() ] [ move.get_x() ] = player;
            propagate_color (move.get_x(), move.get_y(), player );
            
            if ( player == WHITE )
            {            
               p_nb_white++;
            }
            else
            {            
               p_nb_black++;
            }
            
        }
        else
        {
            // Manage piece movement. Add a piece to destination and remove source
            p_board [ move.get_destination_y() ] [ move.get_destination_x() ] = player;
            p_board [ move.get_y() ] [ move.get_x() ] = PLAYABLE;
            propagate_color ( move.get_destination_x(), move.get_destination_y(), player );
            
        }        
                
    }
    
    /**
     * Change the color of the player by returning the opposite color of 
     * the color passed in parameter
     * 
     * @param player current player to switch
     * @return new player color.
     * 
     */
    
    public static char switch_player ( char player )
    {
        if ( player == Board.BLACK )
           return Board.WHITE;
        else
           return Board.BLACK;
    }
    
    /**
     * Draw the current status of the board on the console. Mostly used for
     * debugging when using a gui.
     */
    public void draw ()
    {
        // Draw the header of the board for column identification
        System.out.print ("  ");
        for ( int i = 0; i < p_width ; i++ )
        {
            System.out.print ( ( i + 1 ) + " " );
        }
                        
        //Print the content of the board and the left line identification column
        for ( int j = 0 ; j < p_height ; j++ )
        {
           System.out.print ( "\n" + Move.digit_to_letter ( j ) + " " ); 
           for ( int i = 0 ; i < p_width; i++ )
           {
               System.out.print ( p_board [ j ][ i ] + " " );
           }
           
        }
        
    }
    
    // Private methods
    
    /**
     * Determines if a space is occupied by any of the player. 
     * 
     * @param x position on the board
     * @param y position on the board
     * 
     * @return true if the space is occupied by a player
     */
    
    private boolean is_occupied ( int x, int y )
    {
        char tmpchar = p_board [ y ] [ x ];
        
        if (tmpchar == Board.WHITE || tmpchar == Board.BLACK )
           return true;
        else
           return false;
    
    }
    
    /**
     * Propagate the color of the player to adjacent pieces after playing a move
     * 
     * @param x position of the move (destination in case of a piece movement)
     * @param y position of the move (destination in case of a piece movement)
     * @param player color of the player
     */
    
    private void propagate_color ( int x, int y, char player )
    {
        int nb_converted = 0; 
       
        //Search an inner area of the board for any piece and change it to the active player's color
        int starty = Math.max ( y - 1, 0 );
        int startx = Math.max ( x - 1 , 0 );
        int endy = Math.min ( y + 1, p_height - 1 );
        int endx = Math.min ( x +1, p_width - 1);
                    
        for ( int j = starty ; j <= endy; j++ )
           for ( int i = startx ; i <= endx; i++ )
              if ( is_occupied ( i, j ) == true )
              {
                 //need to verify space since I must count the number of 
                 //permutation because the count is necessary for the AI
                 if ( p_board [ j ] [ i ] != player )
                 {
                    p_board [ j ] [ i ] = player;
                    nb_converted++;
                 }   
                 
              }
        
        if ( player == WHITE )
        {            
           p_nb_white += nb_converted;
           p_nb_black -= nb_converted;
        }
        else
        {            
           p_nb_black += nb_converted;
           p_nb_white -= nb_converted;
        }
        
    }
} // Board
