


/**
 * A T A X X _ G U I
 *
 * <p>This is the 2 player main class for the Gui version of the game.
 * This version includes an Artificial Intelligence for solo play.</p>
 * 
 * <p>The goal of the game is to expand and convert your opponent's pieces
 * by duplicating your pieces adjacent to your ennemy or
 * by making your pieces jump up to 2 space to a location adjacent to your opponent
 * When a player cannot make any move, the game ends.</p?
        
   <br>Play syntax

   <br>Adding a piece can be done using a letter and a digit like 'd3' in any order
   <br>Movement requires a pair of coordinates, source first then destination.
   
   <br>For Example: 'd3 c5'
        
   <br><br>Enjoy! and have fun
 * 
 *
 * 
 * @version 18 April 2017
 * <br>( GNU General Public License )
 * 
 * @author Eric Pietrocupo
 * <br>( ericp@lariennalibrary.com )
 *
 */
 
public class Ataxx_gui 
{
       
    /**
     * Main Program. The main processing has been moved to the BoardWindowManager. So
     * this method only starts the setup window and that's all.
     * 
     * <p>The game flow works as follow: The main open the setup window, which close
     * itself and open the game board window. When the game ends, a dialog box is show
     * and the program is closed</p>
     * 
     * @param params command line parameters
     */
    
    
    public static void main (String[] params) 
    {   
        new SetupWindow();           
    } 
    
} // Ataxx_2p
