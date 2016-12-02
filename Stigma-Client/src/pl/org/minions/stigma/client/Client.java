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

import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import pl.org.minions.stigma.chat.Chat;
import pl.org.minions.stigma.chat.client.ChatProcessor;
import pl.org.minions.stigma.client.images.ImageDB;
import pl.org.minions.stigma.client.observers.AvailableActorsObserver;
import pl.org.minions.stigma.client.observers.CommandHandler;
import pl.org.minions.stigma.client.observers.CurrentSegmentChangeObserver;
import pl.org.minions.stigma.client.observers.LoginStateObserver;
import pl.org.minions.stigma.client.ui.ClientUI;
import pl.org.minions.stigma.client.ui.event.UiEventDispatcher;
import pl.org.minions.stigma.client.ui.event.UiEventRegistry;
import pl.org.minions.stigma.databases.actor.ArchetypeDB;
import pl.org.minions.stigma.databases.actor.ProficiencyDB;
import pl.org.minions.stigma.databases.actor.client.ArchetypeDBAsync;
import pl.org.minions.stigma.databases.actor.client.ProficiencyDBAsync;
import pl.org.minions.stigma.databases.item.ItemFactory;
import pl.org.minions.stigma.databases.item.client.ItemTypeDBAsync;
import pl.org.minions.stigma.databases.item.client.ModifierCategoryDBAsync;
import pl.org.minions.stigma.databases.item.client.ModifierDBAsync;
import pl.org.minions.stigma.databases.map.MapDB;
import pl.org.minions.stigma.databases.map.client.MapDBAsync;
import pl.org.minions.stigma.databases.xml.client.SimpleXmlAsyncDB;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.command.Command;
import pl.org.minions.stigma.game.item.Item;
import pl.org.minions.stigma.game.map.MapInstance;
import pl.org.minions.stigma.game.map.MapInstance.Segment;
import pl.org.minions.stigma.game.world.World;
import pl.org.minions.stigma.globals.LoginState;
import pl.org.minions.stigma.network.Connector;
import pl.org.minions.stigma.network.GlobalConnector;
import pl.org.minions.stigma.network.client.ClientGlobalConnector;
import pl.org.minions.stigma.network.messaging.NetworkMessage;
import pl.org.minions.stigma.network.messaging.NetworkMessageReceiver;
import pl.org.minions.stigma.network.messaging.auth.LoginActorChosen;
import pl.org.minions.stigma.network.messaging.auth.LoginActorsList;
import pl.org.minions.stigma.network.messaging.auth.LoginBadVersion;
import pl.org.minions.stigma.network.messaging.auth.LoginCorrect;
import pl.org.minions.stigma.network.messaging.auth.LoginData;
import pl.org.minions.stigma.network.messaging.auth.LoginDisabledActorList;
import pl.org.minions.stigma.network.messaging.auth.LoginError;
import pl.org.minions.stigma.network.messaging.auth.LoginPasswordAccepted;
import pl.org.minions.stigma.network.messaging.auth.LoginProceed;
import pl.org.minions.stigma.network.messaging.auth.LoginRefreshDisabledActorList;
import pl.org.minions.stigma.network.messaging.auth.LoginRequest;
import pl.org.minions.stigma.network.messaging.chat.ChatMessage;
import pl.org.minions.stigma.network.messaging.game.CommandMessage;
import pl.org.minions.stigma.network.messaging.system.Disconnected;
import pl.org.minions.stigma.network.messaging.system.LogoutRequest;
import pl.org.minions.stigma.network.messaging.system.SystemMessage;
import pl.org.minions.utils.collections.ThreadSafeCollection;
import pl.org.minions.utils.logger.Log;

/**
 * A class representing a Stigma game client. Independent
 * from user interface implementation.
 */
public class Client implements NetworkMessageReceiver
{
    /**
     * Client error message strings collection.
     */
    protected static final class ClientErrors
    {
        public static final String UNHANDLED_MESSAGE_TYPE =
                "Nothing can handle message with type: {0}";

        public static final String BAD_MESSAGE_TYPE = "Bad type: {0}";
        public static final String UNEXPECTED_MESSAGE_DURING_LOGIN =
                "Unexpected message: {0}  while login state is: {1}";
        public static final String UNEXPECTED_SYSTEM_MESSAGE_TYPE =
                "Unexpected system message type: {0}";

        private ClientErrors()
        {
        }
    }

    private class CommandQueueThread implements Runnable
    {
        /** {@inheritDoc} */
        @Override
        public void run()
        {
            while (!Thread.interrupted())
            {
                Command command = null;
                try
                {
                    command =
                            Client.this.commandQueue.poll(taskForCommandThread() ? COMMAND_THREAD_POLL_TIMEOUT
                                                                                : COMMAND_THREAD_POLL_LONG_TIMEOUT,
                                                          COMMAND_THREAD_POLL_UNIT);
                }
                catch (InterruptedException e)
                {
                    if (Log.isDebugEnabled())
                        Log.logger.debug(getClass().getName() + " interrupted");
                    return;
                }

                if (command != null)
                {
                    applyCommand(command);
                }

                updateLoadingProgress();

                if (mapLoader.checkIfShouldChangeMap()) //TODO: maybe only after command
                    mapLoader.updateCurrentMapInstance();
                if (!mapLoader.isMapChanging()
                    && !mapLoader.isMapInstanceComplete())
                    mapLoader.completeMapInstance();

                updateCurrentSegment();

                if (command != null)
                {
                    synchronized (playerActorNotNullLock)
                    {
                        if (playerActor == null)
                            return;

                        for (CommandHandler handler : commandHandlers)
                            handler.handleCommand(command);

                        if (command.getRequesterId() == playerActor.getId())
                            playerController.playerCommandResponse(command);
                    }

                    if (dispatcher != null)
                        dispatcher.dispatch(command);
                }

                Thread.yield();
            }
        }

        private boolean taskForCommandThread()
        {
            return !mapLoader.isMapReady() || currentMapSegment == null
                || !loadingDBs.isEmpty();
        }
    }

    private static final int COMMAND_THREAD_POLL_TIMEOUT = 200;
    private static final int COMMAND_THREAD_POLL_LONG_TIMEOUT = 1000;
    private static final TimeUnit COMMAND_THREAD_POLL_UNIT =
            TimeUnit.MILLISECONDS;

    private static Client instance;

    private ClientUI ui;
    private ClientStateManager stateManager;

    private LoginState loginState = LoginState.CONNECTED;
    private Collection<LoginStateObserver> loginStateObservers =
            Collections.synchronizedList(new LinkedList<LoginStateObserver>());
    private boolean authResponseSent;

    private Connector connector;

    private final URL gameServerURL;
    private World world;

    private MapDBAsync mapDB;
    private ArchetypeDBAsync archetypeDB;

    private ItemTypeDBAsync itemTypeDB;

    private ModifierCategoryDBAsync modifierCategoryDB;
    private ModifierDBAsync modifierDB;
    private ProficiencyDBAsync proficiencyDB;

    private Collection<SimpleXmlAsyncDB<?, ?, ?>> loadingDBs =
            Collections.synchronizedSet(new HashSet<SimpleXmlAsyncDB<?, ?, ?>>());
    private Collection<SimpleXmlAsyncDB<?, ?, ?>> loadedDBs =
            Collections.synchronizedSet(new HashSet<SimpleXmlAsyncDB<?, ?, ?>>());

    private final Object worldAlterationLock = new Object();

    private Actor playerActor;

    private MapLoader mapLoader = new MapLoader();

    private MapInstance.Segment currentMapSegment;

    private final Collection<CurrentSegmentChangeObserver> currentSegmentChangeObservers =
            new ThreadSafeCollection<CurrentSegmentChangeObserver>();

    private final Collection<Actor> playerAvailableActors =
            new ThreadSafeCollection<Actor>();
    private final Collection<Integer> playerDisabledActorsIds =
            new ThreadSafeCollection<Integer>();

    private final Collection<AvailableActorsObserver> availableActorsObservers =
            new LinkedList<AvailableActorsObserver>();

    private final Object playerActorNotNullLock = new Object();

    private BlockingQueue<Command> commandQueue =
            new LinkedBlockingQueue<Command>();

    private Thread commandThread;

    private Collection<CommandHandler> commandHandlers =
            new ThreadSafeCollection<CommandHandler>(new LinkedList<CommandHandler>());

    private Thread cooldownThread;

    private PlayerController playerController;

    private ClientDataRequestSink dataRequestSink = new ClientDataRequestSink();

    private long lastCommandSentTimestamp;
    private UiEventDispatcher dispatcher;
    private ChatProcessor chatProcessor;

    /**
     * Creates client instance.
     * <p>
     * Client must be provided with a {@link ClientUI}
     * implementation before starting to function.
     * @param gameServerURL
     *            server URL
     */
    public Client(URL gameServerURL)
    {
        assert instance == null;
        instance = this;
        this.gameServerURL = gameServerURL;
    }

    /**
     * Sets the ClientUI instance for use by this Client
     * object.
     * <p>
     * Once the UI is set for a client it cannot be changed.
     * @param ui
     *            the ui to set
     */
    final void setUI(ClientUI ui)
    {
        if (this.ui != null)
            throw new IllegalStateException("ClientUI already set.");
        this.ui = ui;
        this.dispatcher = ui.createDispatcher();
        this.chatProcessor = new ChatProcessor(this.dispatcher);

        stateManager = new ClientStateManager(this, ui);

        ui.initialize();
    }

    /**
     * Starts all client threads.
     */
    public synchronized void start()
    {
        initCommandThread();

        assert ui != null;
        ui.start();
    }

    /**
     * Stops all client threads and terminates connections.
     */
    public synchronized void stop()
    {
        if (playerController != null)
            playerController.stop();

        assert ui != null;

        ui.stop();
        if (connector != null)
            connector.closeConnection();
        if (GlobalConnector.globalInstance() != null)
            GlobalConnector.globalInstance().stop();
        if (commandThread != null)
            commandThread.interrupt();
        if (cooldownThread != null)
            cooldownThread.interrupt();
    }

    /**
     * Returns global client instance.
     * @return global client instance
     */
    public static Client globalInstance()
    {
        assert instance != null;
        return instance;
    }

    /**
     * Returns world.
     * @return world
     */
    public final World getWorld()
    {
        return world;
    }

    /**
     * Returns player controller.
     * @return player controller
     */
    public final PlayerController getPlayerController()
    {
        return playerController;
    }

    /**
     * Returns current state of login process.
     * @return the login state
     */
    public final LoginState getLoginState()
    {
        return loginState;
    }

    /**
     * Returns an {@link ArchetypeDB} object used by this
     * client. This object might change between sessions.
     * @return an archetype database object
     */
    public final ArchetypeDB getArchetypeDB()
    {
        return archetypeDB;
    }

    /**
     * Returns an {@link ProficiencyDB} object used by this
     * client. This object might change between sessions.
     * @return an proficiency database object
     */
    public final ProficiencyDB getProficiencyDB()
    {
        return proficiencyDB;
    }

    /**
     * Returns a {@link MapDB} object used by this client.
     * This object might change upon reconnection.
     * <p>
     * Note: MapDB can be retrieved by calling
     * <code>getWorld().getMapDB()</code>.
     * @see #getWorld()
     * @return a map database object
     */
    @Deprecated
    public final MapDB getMapDB()
    {
        return mapDB;
    }

    /**
     * Returns playerActor.
     * @return playerActor
     */
    public final Actor getPlayerActor()
    {
        return playerActor;
    }

    /**
     * Returns map segment on which player actor is on.
     * @return map segment on which player actor is on.
     */
    public MapInstance.Segment getPlayerSegment()
    {
        assert playerActor != null;
        assert world != null;
        return getWorld().getMap(playerActor.getMapId(),
                                 playerActor.getMapInstanceNo())
                         .getSegmentForPosition(playerActor.getPosition());
    }

    /**
     * Returns collection of actors available to player.
     * <p>
     * The collection is thread safe.
     * @return actors available to player, both enabled and
     *         disabled
     */
    public final Collection<Actor> getPlayerAvailableActors()
    {
        return playerAvailableActors;
    }

    /**
     * Checks if the player actor with given Id is disabled
     * now.
     * @param id
     *            actor id
     * @return <code>true</code> if actor is not disabled
     *         for any reason
     */
    public final boolean isPlayerActorEnabled(int id)
    {
        return !playerDisabledActorsIds.contains(id);
    }

    /**
     * Checks if client has an active connection to server.
     * @return <code>true</code> when Client is connected to
     *         server
     */
    public boolean isConnected()
    {
        return connector != null && connector.isConnected();
    }

    /**
     * Requests server to log out.
     * <p>
     * Call delegated to {@link ClientStateManager#logOut()}
     */
    public final void logout()
    {
        stateManager.logOut();
    }

    /**
     * Requests server to log out.
     */
    final void doLogOut()
    {
        send(new LogoutRequest());
    }

    /**
     * Reconnects to server, using URL given in constructor.
     * Authentication will be needed.
     * <p>
     * Call delegated to
     * {@link ClientStateManager#reconnect()}
     * @return <code>true</code> if connected successfully,
     *         <code>false</code> otherwise.
     */
    public boolean reconnect()
    {
        return stateManager.reconnect();
    }

    /**
     * Reconnects to server, using URL given in constructor.
     * Authentication will be needed.
     * @return <code>true</code> if connected successfully,
     *         <code>false</code> otherwise.
     */
    final boolean doReconnect()
    {
        if (connector != null)
        {
            connector.setNetworkMessageReceiver(null);
            connector.closeConnection();
        }

        GlobalConnector.destroyGlobalInstance();

        connector =
                ClientGlobalConnector.initGlobalInstance(gameServerURL, this);

        if (!isConnected())
        {
            setLoginState(LoginState.LOGIN_FAILED);
            return false;
        }
        else
        {
            setLoginState(LoginState.CONNECTED);
            return true;
        }
    }

    /**
     * Adds an available actors list observer.
     * @param observer
     *            observer to add
     */
    public void addAvailableActorsObserver(AvailableActorsObserver observer)
    {
        if (Log.isDebugEnabled())
            Log.logger.debug("New AvailableActorsObserver: "
                + observer.getClass());
        availableActorsObservers.add(observer);
    }

    /**
     * Adds a command handler.
     * @param h
     *            handler to add
     */
    public void addCommandHandler(CommandHandler h)
    {
        if (Log.isDebugEnabled())
            Log.logger.debug("New CommandHandler: " + h.getClass());
        commandHandlers.add(h);
    }

    /**
     * Adds an observer for current map segment change.
     * @param observer
     *            observer to add
     */
    public void addCurrentSegmentChangeObserver(CurrentSegmentChangeObserver observer)
    {
        if (Log.isDebugEnabled())
            Log.logger.debug("New CurrentSegmentChangeObserver: "
                + observer.getClass());

        currentSegmentChangeObservers.add(observer);
    }

    /**
     * Adds a login state observer.
     * @param o
     *            observer to add
     */
    public void addLoginStateObserver(LoginStateObserver o)
    {
        if (Log.isDebugEnabled())
            Log.logger.debug("New LoginStateObserver: " + o.getClass());
        loginStateObservers.add(o);
    }

    /**
     * Removes an available actors list observer.
     * @param observer
     *            observer to add
     */
    public void removeAvailableActorsObserver(AvailableActorsObserver observer)
    {
        availableActorsObservers.remove(observer);
    }

    /**
     * Removes a command handler.
     * @param h
     *            handler to remove
     */
    public void removeCommandHandler(CommandHandler h)
    {
        commandHandlers.remove(h);
    }

    /**
     * Returns loadingDBs.
     * @return loadingDBs
     */
    public final int getLoadingProgressMax()
    {
        return loadedDBs.size() + loadingDBs.size();
    }

    /**
     * Returns loadedDBs.
     * @return loadedDBs
     */
    public final int getLoadingProgress()
    {
        return loadedDBs.size();
    }

    /**
     * Returns mapLoader.
     * @return mapLoader
     */
    public MapLoader getMapLoader()
    {
        return mapLoader;
    }

    /**
     * Returns client state manager.
     * @return state manager
     */
    public ClientStateManager getStateManager()
    {
        return stateManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void networkMessage(NetworkMessage msg)
    {
        //Log.logger.debug("Received message: " + msg.getType().name());
        switch (msg.getType())
        {
            case LOGIN_REQUEST:
                handleAuthRequest((LoginRequest) msg);
                return;
            case LOGIN_BAD_VERSION:
                handleAuthBadVersion((LoginBadVersion) msg);
                return;
            case LOGIN_PASSWORD_ACCEPTED:
                handleAuthPasswordAccepted((LoginPasswordAccepted) msg);
                return;
            case LOGIN_ERROR:
                handleAuthError((LoginError) msg);
                return;
            case LOGIN_CORRECT:
                handleAuthCorrect((LoginCorrect) msg);
                return;
            case LOGIN_DISABLED_ACTOR_LIST:
                handleDisabledActorList((LoginDisabledActorList) msg);
                return;
            case LOGIN_ACTORS_LIST:
                handleActorsList((LoginActorsList) msg);
                return;
            case DISCONNECTED:
                handleDisconnected((Disconnected) msg);
                return;
            case COMMAND:
                handleCommand((CommandMessage) msg);
                return;
            case CHAT:
                handleChat((ChatMessage) msg);
                return;
            case SYSTEM_MESSAGE:
                handleSystemMessage((SystemMessage) msg);
                return;
            default:
                Log.logger.error(MessageFormat.format(ClientErrors.UNHANDLED_MESSAGE_TYPE,
                                                      msg.getType().toString()));
                return;
        }
    }

    /**
     * Handles special system messages like confirmation
     * that player logged out properly.
     * @param msg
     *            server message
     */
    private void handleSystemMessage(SystemMessage msg)
    {
        switch (msg.getSystemEventType())
        {
            case WORLD_SAVED:
                if (Log.logger.isDebugEnabled())
                    Log.logger.debug("World saved received!");
                //TODO handle information about world saved on client
                return;
            case ACTOR_SAVED:
                if (Log.logger.isDebugEnabled())
                    Log.logger.debug("Actor saved received!");
                return;
            case LOGGED_OUT:
                stateManager.confirmLogOut();
                return;
            default:
                Log.logger.error(MessageFormat.format(ClientErrors.UNEXPECTED_SYSTEM_MESSAGE_TYPE,
                                                      msg.getSystemEventType()));
        }
    }

    private void handleChat(ChatMessage msg)
    {
        dispatcher.dispatch(msg.getChat());
    }

    /**
     * Called when server informs client that it is out of
     * date.
     * @param msg
     *            server message
     */
    private void handleAuthBadVersion(LoginBadVersion msg)
    {
        if (getLoginState() == LoginState.KEYS_EXCHANGED)
            setLoginState(LoginState.BAD_CLIENT_VERSION);
        else
            Log.logger.error(MessageFormat.format(ClientErrors.UNEXPECTED_MESSAGE_DURING_LOGIN,
                                                  msg.toString(),
                                                  getLoginState().toString()));
        System.exit(1);
    }

    /**
     * Called after authenticated player chooses a valid
     * actor.
     * @param msg
     *            server message
     */
    private void handleAuthCorrect(LoginCorrect msg)
    {
        if (getLoginState() == LoginState.CHOOSING_ACTOR)
        {
            int chosenActorId = msg.getActorId();
            assert playerActor == null;

            for (Actor actor : playerAvailableActors)
            {
                if (actor.getId() == chosenActorId)
                {
                    playerActor = actor;
                    world.addActor(playerActor);
                    break;
                }
            }
            assert playerActor != null;
            assert !playerDisabledActorsIds.contains(playerActor.getId());
            mapLoader.reset();
            playerAvailableActors.clear();
            setLoginState(LoginState.LOGGED_IN);
            playerController = new PlayerController();
            dispatcher.addCooldownChangedListener(playerController);
            mapLoader.addMapLoadingObsrever(playerController);

            msg.applyToActor(playerActor);
            for (Item i : playerActor.getAllItems())
            {
                world.addItem(i);
                dataRequestSink.getItemSink().forceAdd(i);
            }

            synchronized (playerActorNotNullLock)
            {
                playerActorNotNullLock.notifyAll();
            }
        }
        else
        {
            Log.logger.error(MessageFormat.format(ClientErrors.UNEXPECTED_MESSAGE_DURING_LOGIN,
                                                  msg.toString(),
                                                  getLoginState().toString()));
            System.exit(1);
        }
    }

    /**
     * Called when server indicates that authentication
     * information presented is incorrect.
     * @param msg
     *            server message
     */
    private void handleAuthError(LoginError msg)
    {
        switch (getLoginState())
        {
            case KEYS_EXCHANGED:
                setLoginState(LoginState.FIRST_RETRY);
                break;
            case FIRST_RETRY:
                setLoginState(LoginState.SECOND_RETRY);
                break;
            case SECOND_RETRY:
                setLoginState(LoginState.LOGIN_FAILED);
                break;
            default:
                Log.logger.error(MessageFormat.format(ClientErrors.UNEXPECTED_MESSAGE_DURING_LOGIN,
                                                      msg.toString(),
                                                      getLoginState().toString()));
                System.exit(1);
                break;
        }
    }

    /**
     * Called when server accepts the authentication info.
     * @param msg
     *            server message
     */
    private void handleAuthPasswordAccepted(LoginPasswordAccepted msg)
    {
        final URI clientResourceUri =
                ClientConfig.globalInstance().getClientResourceUri();
        switch (getLoginState())
        {
            case KEYS_EXCHANGED:
            case FIRST_RETRY:
            case SECOND_RETRY:

                URI uri = ClientConfig.globalInstance().getClientResourceUri();
                mapDB = new MapDBAsync(uri);
                archetypeDB = new ArchetypeDBAsync(uri);
                itemTypeDB = new ItemTypeDBAsync(uri);
                modifierDB = new ModifierDBAsync(uri);
                modifierCategoryDB =
                        new ModifierCategoryDBAsync(clientResourceUri);
                proficiencyDB = new ProficiencyDBAsync(uri);

                if (dispatcher != null)
                {
                    dispatcher.connectDB(itemTypeDB);
                }

                ItemFactory.initialize(itemTypeDB, modifierDB);

                world = new World(mapDB);

                loadingDBs.add(modifierCategoryDB);
                loadingDBs.addAll(modifierDB.getSubDBs());
                loadingDBs.add(proficiencyDB);
                loadedDBs.clear();
                break;
            default:
                Log.logger.error(MessageFormat.format(ClientErrors.UNEXPECTED_MESSAGE_DURING_LOGIN,
                                                      msg.toString(),
                                                      getLoginState().toString()));
                System.exit(1);
                break;
        }

        setLoginState(LoginState.PASSWORD_ACCEPTED);
    }

    /**
     * Called when server sends list of available actors
     * (after login proceed).
     * @param msg
     *            server message
     */
    private void handleActorsList(LoginActorsList msg)
    {
        if (getLoginState() != LoginState.PASSWORD_ACCEPTED)
        {
            Log.logger.error(MessageFormat.format(ClientErrors.UNEXPECTED_MESSAGE_DURING_LOGIN,
                                                  msg.toString(),
                                                  getLoginState().toString()));
            System.exit(1);
            return;
        }

        playerActor = null;
        playerAvailableActors.clear();
        playerDisabledActorsIds.clear();
        playerAvailableActors.addAll(msg.getAvailableActors());
        Set<Short> archetypeIds = new HashSet<Short>();
        for (Actor actor : playerAvailableActors)
        {
            archetypeIds.addAll(actor.getPersistenArchetypes());
        }
        for (Short id : archetypeIds)
        {
            archetypeDB.getArchetype(id);
        }
        playerDisabledActorsIds.addAll(msg.getDisabledActors());
        setLoginState(LoginState.CHOOSING_ACTOR);
        for (AvailableActorsObserver actorsObserver : availableActorsObservers)
            actorsObserver.playerAvailableActorsChange();
    }

    /**
     * Called when server sends request for authentication
     * info.
     * @param msg
     *            server message
     */
    private void handleAuthRequest(LoginRequest msg)
    {
        if (getLoginState() == LoginState.CONNECTED)
        {
            setLoginState(LoginState.KEYS_EXCHANGED);
            ClientConfig.setGlobalConfig(msg.getConfig());
            ImageDB.globalInstance().clear();
        }
        else
        {
            Log.logger.error(MessageFormat.format(ClientErrors.UNEXPECTED_MESSAGE_DURING_LOGIN,
                                                  msg.toString(),
                                                  getLoginState().toString()));
            System.exit(1);
        }
    }

    /**
     * Applies the command to World, if it succeeded.
     * @param command
     *            command to apply
     */
    private void applyCommand(Command command)
    {
        if (Log.isTraceEnabled())
            Log.logger.trace("Applying command: " + command.getType().name());

        if (!command.hasFailed())
        {
            boolean applyOK = false;

            synchronized (worldAlterationLock)
            {
                applyOK = command.apply(getWorld(), dataRequestSink);
            }
            if (applyOK)
            {
                if (Log.isTraceEnabled())
                    Log.logger.trace("Command applied OK: "
                        + command.getType().name());
                sendDataRequest();
            }
            else
                Log.logger.warn("Applying command FAILED: "
                    + command.getType().name());
        }
    }

    /**
     * @param msg
     */
    private void handleCommand(CommandMessage msg)
    {
        Command command = msg.getCommand();
        if (Log.isTraceEnabled())
            Log.logger.trace("Received command: " + command.toString());

        commandQueue.add(command);
    }

    /**
     * @param msg
     */
    private void handleDisabledActorList(LoginDisabledActorList msg)
    {
        playerDisabledActorsIds.clear();
        playerDisabledActorsIds.addAll(msg.getDisabledActors());
        for (AvailableActorsObserver actorsObserver : availableActorsObservers)
            actorsObserver.playerAvailableActorsChange();
    }

    /**
     * Called when server closes connection on the
     * application level.
     * @param msg
     *            server message
     */
    private void handleDisconnected(Disconnected msg)
    {
        synchronized (playerActorNotNullLock)
        {
            if (playerActor != null)
            {
                dispatcher.removeCooldownChangedListener(playerController);
                playerController.stop();
                mapLoader.removeMapLoadingObserver(playerController);
                playerController = null;
                playerActor = null;
            }
        }
        stateManager.connectionToServerLost();
        setLoginState(LoginState.LOGIN_FAILED);
        Log.logger.debug("Server closed connection.");
    }

    /**
     * Returns <code>true</code> if the client has sent a
     * response to the server and is awaiting new login
     * state.
     * @return whether the authentication response was sent
     */
    public final boolean wasAuthResponseSent()
    {
        return authResponseSent;
    }

    /**
     * Sends authentication data to server.
     * <p>
     * Call delegated to
     * {@link ClientStateManager#sendAuthenticationData(String, String)}
     * @param userName
     *            user name
     * @param password
     *            user password
     */
    public void sendAuthData(String userName, String password)
    {
        stateManager.sendAuthenticationData(userName, password);
    }

    /**
     * Sends authentication data to server.
     * @param userName
     *            user name
     * @param password
     *            user password
     */
    void doSendAuthData(String userName, String password)
    {
        if (authResponseSent)
            return;
        send(new LoginData(userName, password));
        authResponseSent = true;
    }

    /**
     * Sends information that client is ready for
     * continuation of login process.
     */
    private void sendLoginProceed()
    {
        if (authResponseSent)
            return;
        send(new LoginProceed());
        authResponseSent = true;
    }

    /**
     * Sends a request to server to provide an up to date
     * list of disabled actors ids for current player
     * (useful while logging in).
     */
    public void requestDisabledActorsListUpdate()
    {
        send(new LoginRefreshDisabledActorList());
    }

    /**
     * Sends id of the chosen actor to server.
     * <p>
     * Call delegated to
     * {@link ClientStateManager#sendActorChoice(int)}.
     * @param chosenActorId
     *            id of the chosen actor
     */
    public void sendChosenActorId(int chosenActorId)
    {
        stateManager.sendActorChoice(chosenActorId);
    }

    /**
     * Sends id of the chosen actor to server.
     * @param chosenActorId
     *            id of the chosen actor
     */

    void doSendChosenActorId(int chosenActorId)
    {
        if (authResponseSent)
            return;
        send(new LoginActorChosen(chosenActorId));
        authResponseSent = true;
    }

    /**
     * Sends a command to server.
     * @param command
     *            command to send.
     */
    public void sendCommand(Command command)
    {
        synchronized (playerActorNotNullLock)
        {
            if (playerActor == null)
                return;
            command.setRequesterId(playerActor.getId());
        }

        if (stateManager.getClientState().equals(ClientState.LOGGING_OUT))
            throw new IllegalStateException("Logging out. Don't send stuff.");

        if (Log.isTraceEnabled())
            Log.logger.trace("Sending command: " + command);

        send(new CommandMessage(command));

        if (Log.isTraceEnabled())
        {
            final long now = System.currentTimeMillis();
            if (lastCommandSentTimestamp > 0)
            {
                if (Log.isTraceEnabled())
                    Log.logger.trace("Command sent to Connector after "
                        + Long.toString(now - lastCommandSentTimestamp) + " ms");
            }
            lastCommandSentTimestamp = now;
        }
    }

    /**
     * Sends chat to server.
     * @param chat
     *            chat to send
     */
    public void sendChat(Chat chat)
    {
        send(new ChatMessage(chat));
    }

    /**
     * Sends the given message to server.
     * @param msg
     *            message to send
     */
    private void send(NetworkMessage msg)
    {
        if (connector != null)
            connector.networkMessage(msg);
        else
            Log.logger.warn("Trying to send network message with empty connector");
    }

    private void sendDataRequest()
    {
        if (!dataRequestSink.getActorSink().isEmpty())
        {
            sendCommand(dataRequestSink.getActorSink());
            dataRequestSink.resetActorSink();
        }
        if (!dataRequestSink.getItemSink().isEmpty())
        {
            sendCommand(dataRequestSink.getItemSink());
            dataRequestSink.resetItemSink();
        }
    }

    /**
     * Sets the remembered login state and notifies
     * observers.
     * @param loginState
     *            the login state to set
     */
    private void setLoginState(LoginState loginState)
    {
        this.loginState = loginState;
        authResponseSent = false;
        if (Log.isDebugEnabled())
            Log.logger.debug("New login state: " + loginState.name());
        for (LoginStateObserver observer : loginStateObservers)
        {
            observer.loginStateChanged(loginState);
        }
    }

    private void updateLoadingProgress()
    {
        if (loginState != LoginState.PASSWORD_ACCEPTED)
            return;

        Iterator<SimpleXmlAsyncDB<?, ?, ?>> it = loadingDBs.iterator();
        while (it.hasNext())
        {
            SimpleXmlAsyncDB<?, ?, ?> db = it.next();
            if (db.isLoaded())
            {
                it.remove();
                loadedDBs.add(db);
            }
        }

        if (loadingDBs.isEmpty())
            sendLoginProceed();
    }

    //TODO: maybe move this to a separate class, as with updateMapInstance -> MapLoader
    private void updateCurrentSegment()
    {
        final MapInstance.Segment previousSegment = currentMapSegment;

        synchronized (playerActorNotNullLock)
        {

            if (playerActor == null)
                currentMapSegment = null;

            else
            {
                final MapInstance currentMapInstance =
                        mapLoader.getCurrentMapInstance();

                if (this.currentMapSegment == null
                    || this.currentMapSegment.getParentMap() != currentMapInstance
                    || !currentMapSegment.contains(getPlayerActor().getPosition()))
                {
                    if (currentMapInstance == null
                        || currentMapInstance.needsType())
                        currentMapSegment = null;
                    else
                        currentMapSegment =
                                currentMapInstance.getSegmentForPosition(playerActor.getPosition());

                }
            }
        }

        if (currentMapSegment != previousSegment)
        {
            if (Log.isDebugEnabled())
                Log.logger.debug("Current segment changed; was: "
                    + previousSegment + "; is: " + currentMapSegment);

            if (playerActor != null && currentMapSegment != null)
                assert currentMapSegment.getActors().contains(playerActor);

            final Set<Segment> disappearingSegments = new HashSet<Segment>();
            final Set<Segment> appearingSegments = new HashSet<Segment>();

            if (currentMapSegment != null)
            {
                for (Segment segment : currentMapSegment.neighborhood())
                {
                    appearingSegments.add(segment);
                }
                appearingSegments.add(currentMapSegment);
            }

            if (previousSegment != null)
            {
                for (Segment segment : previousSegment.neighborhood())
                {
                    disappearingSegments.add(segment);
                    appearingSegments.remove(segment);
                }
                disappearingSegments.add(previousSegment);

                if (currentMapSegment != null)
                {
                    for (Segment segment : currentMapSegment.neighborhood())
                    {
                        disappearingSegments.remove(segment);
                    }

                }
            }

            for (final CurrentSegmentChangeObserver observer : currentSegmentChangeObservers)
                if (dispatcher != null)
                    dispatcher.performInDispatchThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            observer.currentSegmentChanged(currentMapSegment,
                                                           previousSegment,
                                                           appearingSegments,
                                                           disappearingSegments);
                        }
                    },
                                                       true);
                else
                    observer.currentSegmentChanged(currentMapSegment,
                                                   previousSegment,
                                                   appearingSegments,
                                                   disappearingSegments);
        }

    }

    private void initCommandThread()
    {
        commandThread = new Thread(new CommandQueueThread(), "commandThread");
        commandThread.start();
    }

    /**
     * Returns interface for adding listeners for events
     * like 'player actor walked'. May return {@code null}
     * when no such interface is available (events are not
     * being send).
     * @return interface for adding listeners for event
     */
    public UiEventRegistry uiEventRegistry()
    {
        return dispatcher;
    }

    /**
     * Returns client's UI.
     * @return client's UI.
     */
    public ClientUI getUi()
    {
        return this.ui;
    }

    /**
     * Returns chat processor.
     * @return chat processor
     */
    public ChatProcessor getChatProcessor()
    {
        return chatProcessor;
    }
}
