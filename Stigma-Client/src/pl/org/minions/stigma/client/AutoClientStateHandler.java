/**
 *   Stigma - Multiplayer online RPG - http://stigma.sourceforge.net
 *   Copyright (C) 2005-2009 Minions Studio
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package pl.org.minions.stigma.client;

import java.util.Collection;

import pl.org.minions.stigma.client.ClientStateManager.ClientStateHandler;
import pl.org.minions.stigma.client.ui.ClientUI;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.utils.logger.Log;

/**
 * Handles {@link ClientState client states} not handled by
 * current {@link ClientUI} implementation.
 * <p>
 * With 'auto login' feature enabled can be use to log in
 * user automatically.
 */
public class AutoClientStateHandler implements ClientStateHandler
{
    private static final String DEFAULT_USER = "test";
    private static final String DEFAULT_PASSWORD = "qwe";

    /** {@inheritDoc} */
    @Override
    public void handleClientState(ClientState state)
    {
        final Client client = Client.globalInstance();
        switch (state)
        {
            case AUTHENTICATION_PROMPT:
                Log.logger.info("Sending auth data: " + DEFAULT_USER + " "
                    + DEFAULT_PASSWORD);
                client.sendAuthData(DEFAULT_USER, DEFAULT_PASSWORD);
                break;
            case CONNECTING:
                Log.logger.info("Loading game data.");
                break;
            case AUTHENTICATION_PROCESSING:
                Log.logger.info("Processing authentication.");
                break;
            case MAP_DATA_LOADING:
                Log.logger.info("Loading map data.");
                break;
            case ACTOR_PROMPT:
                Collection<Actor> actors = client.getPlayerAvailableActors();
                boolean actorChoosen = false;
                if (actors.size() > 0)
                {
                    for (Actor actor : actors)
                    {
                        if (client.isPlayerActorEnabled(actor.getId()))
                        {
                            Log.logger.info("Logging as actor: ["
                                + actor.getId() + "] " + actor.getName());
                            client.sendChosenActorId(actor.getId());
                            actorChoosen = true;
                            break;
                        }
                    }
                }

                if (!actorChoosen)
                {
                    Log.logger.fatal("No enabled actors to chose from!");
                    System.exit(-1);
                }
                break;
            case ACTOR_PROCESSING:
                Log.logger.info("Processing actor selection.");
                break;
            case GAME_DATA_LOADING:
                Log.logger.info("Loading game data");
                break;
            case AUTHENTICATION_ERROR:
                Log.logger.info("Authentication error.");
                client.getStateManager().authErrorConfirm();
                break;
            case DISCONNECTED:
                Log.logger.error("Disconnected.");
                System.exit(-1);
                break;
            case GAME_IN_PROGRESS:
                break;
            case LOGGING_OUT:
                Log.logger.info("Logging out");
                break;
            default:
                Log.logger.fatal("Bad client state!");
                System.exit(-1);
                break;
        }
    }
}
