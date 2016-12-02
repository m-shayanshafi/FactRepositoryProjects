package net.sf.bloodball;

import java.util.HashMap;
import java.util.Map;
import net.sf.bloodball.gameflow.*;
import net.sf.bloodball.gameflow.EndTurnOperation;
import net.sf.bloodball.gameflow.InTurnOperation;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.resources.ResourceKeys;

public class OperationMessages {
  private Map endTurnOperationTexts;
  private Map inTurnOperationMessageBuilder;
  private GameFlowController controller;

  public OperationMessages(GameFlowController controller) {
    this.controller = controller;
    initEndTurnOperationTexts();
    initInTurnOperationMessages();
  }

  public String getEndTurnTextFor(EndTurnOperation operation) {
    return MessageBuilder.getResourceString((String) endTurnOperationTexts.get(operation));
  }

  public String getInTurnStatusTextFor(InTurnOperation operation) {
    if (operation == null) {
      return "";
    }
    return ((MessageBuilder) inTurnOperationMessageBuilder.get(operation)).buildMessage(controller.getGame());
  }

  private void initEndTurnOperationTexts() {
    endTurnOperationTexts = new HashMap();
    endTurnOperationTexts.put(EndTurnOperation.FINISH_BALL_SETUP, ResourceKeys.FINISH_BALL_SETUP);
    endTurnOperationTexts.put(EndTurnOperation.FINISH_TEAM_SETUP, ResourceKeys.FINISH_TEAM_SETUP);
    endTurnOperationTexts.put(EndTurnOperation.END_TURN, ResourceKeys.END_TURN);
    endTurnOperationTexts.put(EndTurnOperation.PICK_UP_BALL, ResourceKeys.PICK_UP_BALL);
  }

  private void initInTurnOperationMessages() {
    inTurnOperationMessageBuilder = new HashMap();
    inTurnOperationMessageBuilder.put(InTurnOperation.BLOCK_AFTER_MOVE, new SimpleMessageBuilder(ResourceKeys.BLOCK_REQUEST));
    inTurnOperationMessageBuilder.put(InTurnOperation.SELECT_ACTIVE_PLAYER, new SimpleMessageBuilder(ResourceKeys.SELECT_ACTIVE_PLAYER));
    inTurnOperationMessageBuilder.put(InTurnOperation.SET_UP_BALL, new SimpleMessageBuilder(ResourceKeys.BALL_SETUP_REQUEST));
    inTurnOperationMessageBuilder.put(InTurnOperation.MOVE, new MessageBuilder() {
      public String buildMessage(Game game) {
        return buildMessage(ResourceKeys.MOVE_COUNTER, controller.getActivePlayer().getRemainingMovePoints());
      }
    });
    inTurnOperationMessageBuilder.put(InTurnOperation.HAND_OFF, new SimpleMessageBuilder(ResourceKeys.HAND_OFF_REQUEST));
    inTurnOperationMessageBuilder.put(InTurnOperation.SUBSTITUTE, new SimpleMessageBuilder(ResourceKeys.SUBSTITUTE_REQUEST));
    inTurnOperationMessageBuilder.put(InTurnOperation.SET_UP_TEAM, new MessageBuilder() {
      public String buildMessage(Game game) {
        return buildMessage(
          ResourceKeys.TEAM_SETUP_REQUEST,
          new Object[] { game.getTeams().getActiveTeam().getName(), new Integer(game.getTeams().getActiveTeam().getPlayersToSetupCount())});
      }
    });
    inTurnOperationMessageBuilder.put(InTurnOperation.SPRINT, new MessageBuilder() {
      public String buildMessage(Game game) {
        return buildMessage(ResourceKeys.SPRINT_COUNTER, controller.getActivePlayer().getRemainingMovePoints());
      }
    });
    inTurnOperationMessageBuilder.put(InTurnOperation.EXTRA_MOVE, new MessageBuilder() {
      public String buildMessage(Game game) {
        return buildMessage(ResourceKeys.EXTRA_MOVE_COUNTER, controller.getActivePlayer().getRemainingMovePoints());
      }
    });
  }

  public void setController(GameFlowController controller) {
    this.controller = controller;
  }
}