package game.run;

import game.model.Action;
import game.model.GameModel;
import game.model.Piece;
import game.model.Player;
import game.model.Point;

import java.net.Socket;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import util.Constants;


public class RemotePlayerController extends PlayerController {
    
    private final NetworkingController networkingController;
    
    private RemotePlayerController(Player player, GameModel model, NetworkingController networkingController){
        super(player, model);
        this.networkingController = networkingController;
        this.networkingController.start();
    }

    public RemotePlayerController(Player player, GameModel model, Socket socket) {
        this(player, model, new NetworkingController(socket)); 
    }

    @Override
    public boolean isAnimatingMove(){
        if (model.lastMoved == null){
            return false;
        }
        List<Point> path = model.findValidMoves(model.lastMoved, model.lastMovedPosition, true).get(model.lastMoved.getPosition());
        return model.sinceLastMoved < Constants.BOARD_MOVE_ANIMATE_TIME * (path.size() - 1);
    }

    @Override
    public void renderPiece(GameContainer container, Graphics g, Piece p){
        if (p.equals(model.lastMoved) && isAnimatingMove()){
            renderPieceMoving(container, g, p, model.lastMovedPosition, p.getPosition(), model.sinceLastMoved);
        } else{
            super.renderPiece(container, g, p);
        }
    }

    @Override
    public Action retrieveAction(){
        Action action = super.retrieveAction();
        if (action == null){
            return networkingController.getAction();
        } else{
            propagateAction(action);
            return action;
        }
    }

    @Override
    public void propagateAction(Action action) {
        networkingController.sendAction(action);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new SetupState(false));
        addState(new PlayBoardState(false));
        addState(new DisconnectedState(false));
        addState(new WonState(false));
    }
}
