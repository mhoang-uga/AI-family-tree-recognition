
/**
 * M o v e 
 *
 * <p>Manages the moves the players can make. Note that moves coordinates are
 * stored as Array coordinates from 0 to N. Conversion of user system coordinates 
 * is done during user input and for display on the screen.</p>
 * 
 * @author Eric Pietrocupo
 * ( ericp@lariennalibrary.com )
 *  
 * @version 10 dec 2016
 * ( GNU General Public Licence )
 * 
 */

public class Move 
{
    
    //Instance Properties
        
    /** X position for placement, X source position for movements) */
    private int p_source_x;
    /** Y position for placement, Y source position for movements) */
    private int p_source_y;
    /** Indicates if this move adds a piece, else it's a movement */
    private boolean p_add_piece;
    /** X destination position for movements only */
    private int p_destination_x;
    /** Y destination position for movements only */
    private int p_destination_y;
    
    //Class Properties
    
    //Constructors
    
    /**
     * Constructor to create a move that add pieces to the board using letter 
     * coordinates for the Y value.
     * 
     * @param x position of the placement
     * @param y position of the placement as a character
     */
    
    public Move ( int x, char y )
    {
        this ( x, letter_to_digit ( y ) );
        
    }
    
    /**
     * Constructor to create a move that adds a piece to the board using both 
     * numeric coordinates.
     * 
     * @param x position of the placement
     * @param y position of the placement
     */
    
    public Move ( int x, int y )
    {
        p_source_x = x;
        p_source_y = y;
        p_add_piece = true;
        p_destination_x = 0;
        p_destination_y = 0;
    }
    
    /**
     * Constructor to create a move that moves a piece from a source location 
     * to a destination using characters as Y coordinates
     * 
     * @param source_x X position of the source location
     * @param source_y Y position of the source location as a character
     * @param dest_x   X position of the destination location
     * @param dest_y   Y position of the destination location as a character
     */
    
    public Move ( int source_x, char source_y, int dest_x, char dest_y ) 
    {
        this ( source_x, letter_to_digit ( source_y ), dest_x,
               letter_to_digit ( dest_y ) );
        
    }
    
    /**
     * Constructor to create a move that moves a piece from a source location
     * to a destination using both numeric coordinates
     * 
     * @param source_x X position of the source location
     * @param source_y Y position of the source location
     * @param dest_x   X position of the destination location
     * @param dest_y   Y position of the destination location
     */
    
    public Move ( int source_x, int source_y, int dest_x, int dest_y ) 
    {
        p_source_x = source_x;
        p_source_y = source_y;
        p_destination_x = dest_x;
        p_destination_y = dest_y;
        p_add_piece = false;
    }
    
    // Getters and setters
    
    public int get_x () 
    {
       return p_source_x;
    }
    
    public int get_y ()
    {
        return p_source_y;
    }
    
    public int get_destination_x ()
    {
        return p_destination_x;
    }
    
    public int get_destination_y ()
    {
        return p_destination_y;
    }
    
        
    /**
     * Returns true if this move add a piece on the board
     * 
     * @return true if move add a piece
     */
    
    public boolean is_add_piece ()
    { 
        return p_add_piece;
    }
    
    // Instance Methods
    
    /**
     * Compare if the content of the object in parameter is the same as this object.
     * 
     * @param obj Move object to compare
     * @return true if both moves are the same
     */
    
    @Override
    public boolean equals ( Object obj )
    {
        boolean is_copy = true;
        
        Move move = (Move) obj;
        
        // check each field manually
        if ( p_source_x != move.p_source_x )
           is_copy = false;
           
        if ( p_source_y != move.p_source_y )
           is_copy = false;
           
        if ( p_destination_x != move.p_destination_x )
           is_copy = false;
        
        if ( p_destination_y != move.p_destination_y )
           is_copy = false;
           
        if ( p_add_piece != move.p_add_piece )
           is_copy = false;           
        
        return is_copy;
    }
    
    /**
     * Convert the content of the move into a string used to display information
     * one the screen or for debugging
     * 
     * @return A string with all the properties
     */
    
    public String toString ( )
    {
        if ( p_add_piece == true )
           return String.format ( "Adding a piece to (%d, %c)",
              p_source_x + 1, 
              digit_to_letter( p_source_y  ));
        else
           return String.format ( "Moving a piece from (%d, %c) to (%d, %c)",
              p_source_x + 1, 
              digit_to_letter (p_source_y ), 
              p_destination_x + 1, 
              digit_to_letter (p_destination_y ) );

    }
    // Class Methods

    /**
     * Converts letter coordinates into an integer
     * 
     * @param value character to convert
     * @return The coordinate as an integer
     */
    public static int letter_to_digit ( char value )
    {
        char tmpchar = Character.toLowerCase ( value );
         
        
        return tmpchar - 'a';
    }
    
    /**
     * Converts a integer coordinate into a character
     * 
     * @param value integer to convert
     * @return The coordinate as a character
     */
    
    public static char digit_to_letter ( int value )
    {
        return ( (char) ( value + 'a' ) );
    }    
    
    
} // Move
