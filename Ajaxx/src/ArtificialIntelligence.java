
import java.util.ArrayList;


/**
 * A r t i f i c i a l I n t e l l i g e n c e
 *
 * <p>Group all the AI algorithm together with an internal node class to 
 * hold the played moved and the status of the board.</p>
 * 
 * @author Eric Pietrocupo
 * (ericp@lariennalibrary.com)
 * 
 * @version 16 april 2017
 * ( GNU General Public License )
 * 
 */

public class ArtificialIntelligence
{
   /** Very large value used to represent infinity */
   public static final int INFINITY = 9999;
   /** number of moves before changing your mind */
   public static final int CHANGE_MIND_COUNT = 10;
   
   /** Used to add some random variety to the selected move, will select
    *  a different move of the same value for each 10 moves. The starting
    *  value is determined randomly, which change the outcome when playing
    *  exactly at the same place. To optimise the speed, it's only used on
    *  top level depth, and a random value is added when the AI change is 
    *  mind. 
    *  */
   private static int p_change_mind = (int) (Math.random() * CHANGE_MIND_COUNT);
   
     
   /** Hold the best found move. Declared as a static variable because cannot
    *  return two parameters during the AI algorithm. Will change value many times
    *  but will only be used once when exiting the AI method.
    */
   private static Move s_bestmove;
   
   /**
    * Use after running an AI algorithm. Contains the best move to play that 
    * was found.
    *  
    * @return The best move that should be played.
    */
   public static Move get_bestmove ()
   {
      return s_bestmove;
   }
   
   /**
    * The negamax algorithm is the same as the min-max algorithm but instead
    * everything is in the same method and the signs and players are switched
    * after each recursive call. It prevent having 2 different copies of 
    * similar methods. 
    * 
    * <pre>
    * --- Pseudo Code --- ( source Wikipedia )
    * 01 function negamax(node, depth, color)
    * 02     if depth = 0 or node is a terminal node
    * 03         return color * the heuristic value of node

    * 04     bestValue := - infinite
    * 05     foreach child of node
    * 06         v := -negamax(child, depth - 1, -color)
    * 07         bestValue := max( bestValue, v )
    * 08     return bestValue
    * 
    * </pre>
    * 
    * @param board current state of the board
    * @param depth of the iteration
    * @param maxdepth Initial depth Started the algorith with
    * @param color of the playing player
    * @return evaluation of the best move
    */
   
   public static int negamax ( Board board, int depth, final int maxdepth, char color)
   {
      int my_besteval;
      Move my_bestmove = null;
      int result;
      ArrayList<Move> my_movelist;
      Board my_board;
      
      // we have reached maximum depth, there for we return the board evaluation
      if ( depth == 0)
      {
         // return the evaluation of the played move
         my_besteval = board.evaluate(color);
         
      }
      else
      {   
         // Search for valid moves
         my_movelist = board.find_valid_moves( color );
         if ( my_movelist.isEmpty() == false)
         {
            //Initialize the best move to an extreme value
            my_besteval = -INFINITY; // Minus infinity
            
            // for each move that was found
            for ( Move my_move: my_movelist )
            {
               //Duplicate the board
               my_board = new Board(board);
               my_board.play(my_move, color);
               
               // recursive call: play the move and evaluate
               result = - negamax ( my_board,  depth -1 , maxdepth, Board.switch_player(color));
                              
               // Check if this move path is better than the move recorded
               // so far              
               if ( result > my_besteval) // use the maximum eval
               {
                  
                  // save a reference to this best move
                  my_bestmove = my_move;
                  // save the best move evaluation result
                  my_besteval = result;
                  
               } 
               // A custom tweak to make the algorithm less predictable
               else if ( depth == maxdepth && result == my_besteval) 
                  // if we are at the top level  and this move is also a good move
               {
                  
                  p_change_mind++; // increment the change mind counter.
                  
                  // we can now change our mind on the final move that will be selected 
                  // by the AI to make sure it's not always the same move after different plays.
                  if ( p_change_mind >= CHANGE_MIND_COUNT)
                     // then change your mind
                  {  
                     my_bestmove = my_move;                     
                     // reset the counter
                     p_change_mind = (int) (Math.random() * CHANGE_MIND_COUNT);
                     
                  }
               }
                  
            }
            // copy the bestmove to static variable to return the value for
            // the initial recursive call. So this will overwrite any previous
            // affectation to the variable and retain the best move of the 
            // player who called the AI.
            s_bestmove = my_bestmove;
         }
         else // No more moves are available, so the game will end
         {  
            my_besteval = board.evaluate(color);
            
            // check if we win or lose the game
            if ( my_besteval  > 0 ) // we win
            {
               my_besteval = INFINITY;
            } 
            else if ( my_besteval < 0 ) // we lose
            {
               my_besteval = -INFINITY; // Minus infinity
            }
         }
         
      }
            
      return my_besteval; 
   }
   
 
}
