/*
 * Pokemon.java
 *
 * Created on December 13, 2006, 5:38 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, visit the Free Software Foundation, Inc.
 * online at http://gnu.org.
 */

package shoddybattle;

import mechanics.*;
import mechanics.moves.*;
import java.util.*;
import java.io.*;
import mechanics.statuses.items.HoldItem;
import mechanics.statuses.StatusEffect;
import mechanics.statuses.StatusListener;
import mechanics.statuses.StatChangeEffect;
import mechanics.statuses.abilities.IntrinsicAbility;
import mechanics.clauses.Clause.PendanticDamageClause;

/**
 * This class represents a pokemon in a battle. Its stats are automatically
 * modified as it attacks and is attacked, so no method for directly modifying
 * its stats are provided. Using this class requires a BattleMechanics object
 * to initialise its stats.
 *
 * @author Colin
 */
public class Pokemon extends PokemonSpecies {
    
    private static final long serialVersionUID = 2636950446169268200L;
    
    // Transient statistics.
    transient private int m_hp;
    transient private int[] m_stat;
    transient private StatMultiplier[] m_multiplier;
    transient private StatMultiplier m_accuracy;
    transient private StatMultiplier m_evasion;
    transient private List m_statuses;
    transient private int[] m_pp, m_maxPp;
    transient private boolean m_fainted;
    transient private BattleField m_field;
    transient private int m_party, m_id;
    transient private IntrinsicAbility m_originalAbility;
    transient private IntrinsicAbility m_ability;
    transient private HoldItem m_item;
    transient private MoveListEntry m_lastMove;
    transient private boolean m_firstTurn = false;
    /** The health of a substitute, or zero if no substitute is out.
     */
    transient private int m_substitute;
    
    // Intrinsic statistics.
    private int m_level = 100;
    private PokemonNature m_nature;
    private MoveListEntry[] m_move;
    private int[] m_ppUp;           // Number of PP Ups applied to each move.
    private String m_abilityName;   // Intrinsic ability.
    private String m_itemName;      // Item initially held by the pokemon.
    private boolean m_shiny = false;
    private int m_gender = GENDER_MALE;
    private String m_nickname;
    
    // Hidden statistics.
    private int m_iv[];
    private int m_ev[];
    
    // Battle mechanics.
    private BattleMechanics m_mech;

    // Constants representing each statistic.
    public static final int S_HP = 0;
    public static final int S_ATTACK = 1;
    public static final int S_DEFENCE = 2;
    public static final int S_SPEED = 3;
    public static final int S_SPATTACK = 4;
    public static final int S_SPDEFENCE = 5;
    public static final int S_ACCURACY = 6;
    public static final int S_EVASION = 7;
    
    /**
     * Create a substitute to take hits for this pokemon.
     */
    public boolean createSubstitute() {
        if (hasSubstitute()) {
            return false;
        }
        int quarter = m_stat[S_HP] / 4;
        if (quarter >= m_hp) {
            return false;
        }
        changeHealth(-quarter);
        m_substitute = quarter;
        return true;
    }
    
    /**
     * Set the health of the substitute.
     */
    public void setSubstitute(int hp) {
        m_substitute = hp;
    }
    
    /**
     * Get the health of the substitute.
     */
    public int getSubstitute() {
        return m_substitute;
    }
    
    /**
     * Return whether this pokemon has a substitute.
     */
    public boolean hasSubstitute() {
        return (m_substitute != 0);
    }
    
    /**
     * Dispose of this object.
     */
    public void dispose() {
        m_multiplier = null;
        m_accuracy = null;
        m_evasion = null;
        m_statuses = null;
        m_field = null;
        m_nature = null;
        m_move = null;
        m_abilityName = null;
        m_itemName = null;
        m_mech = null;
    }
    
    /**
     * Get the name of a stat.
     */
    public static String getStatName(int stat) {
        switch (stat) {
            case S_HP: return "HP";
            case S_ATTACK: return "attack";
            case S_DEFENCE: return "defence";
            case S_SPEED: return "speed";
            case S_SPATTACK: return "special attack";
            case S_SPDEFENCE: return "special defence";
            case S_ACCURACY: return "accuracy";
            case S_EVASION: return "evasion";
        }
        return "";
    }
    
    /**
     * Get the shortened name of a stat.
     */
    public static String getStatShortName(int stat) {
        switch (stat) {
            case S_HP: return "HP";
            case S_ATTACK: return "Atk";
            case S_DEFENCE: return "Def";
            case S_SPEED: return "Spd";
            case S_SPATTACK: return "SAtk";
            case S_SPDEFENCE: return "SDef";
            case S_ACCURACY: return "Acc";
            case S_EVASION: return "Evas";
        }
        return "";
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
    
    /**
     * Unserialises a Pokemon.
     */
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (m_ppUp == null) {
            m_ppUp = new int[m_move.length];
            Arrays.fill(m_ppUp, 3);
        }
        if (m_nickname == null) {
            m_nickname = getSpeciesName();
        } else {
            m_nickname = m_nickname.trim();
            if (m_nickname.length() == 0) {
                m_nickname = getSpeciesName();
            }
        }
        try {
            initialise();
        } catch (StatException e) {
            throw new IOException();
        }
    }
    
    /** Creates a new instance of Pokemon */
    public Pokemon(BattleMechanics mech,
            PokemonSpecies species,
            PokemonNature nature,
            String ability,
            String item,
            int gender,
            int level,
            int[] ivs, int[] evs,
            MoveListEntry[] moves,
            int[] ppUps,
            boolean validate
            ) throws StatException {      
        super(species);
        m_mech = mech;
        m_iv = ivs;
        m_ev = evs;
        m_nature = nature;
        m_gender = gender;
        m_level = level;
        m_move = moves;
        m_abilityName = ability;
        m_itemName = item;
        m_ppUp = ppUps;
        m_nickname = getSpeciesName();
        initialise();
    }
    
    /**
     * Create and validate a new pokemon.
     */
    public Pokemon(BattleMechanics mech,
            PokemonSpecies species,
            PokemonNature nature,
            String ability,
            String item,
            int gender,
            int level,
            int[] ivs, int[] evs,
            MoveListEntry[] moves,
            int[] ppUps
            ) throws StatException {
        this(mech, species, nature, ability, item, gender, level,
                ivs, evs, moves, ppUps, true);
    }
    
    /**
     * Get a random Pokemon object.
     */
    public static Pokemon getRandomPokemon(ModData data, BattleMechanics mech) {
        Random random = mech.getRandom();
        int[] ivs = new int[6];
        for (int i = 0; i < ivs.length; ++i) {
            ivs[i] = random.nextInt(32);
        }
        int[] evs = new int[6];
        int evTotal = 0;
        final int inc = 16;
        while ((evTotal + inc) <= 510) {
            evs[random.nextInt(evs.length)] += inc;
            evTotal += inc;
        }
        PokemonNature nature = PokemonNature.getNature(random.nextInt(25));
        PokemonSpeciesData speciesData = data.getSpeciesData();
        PokemonSpecies species = new PokemonSpecies(
                speciesData,
                random.nextInt(speciesData.getSpeciesCount()));
        TreeSet moveset = species.getLearnableMoves(speciesData);
        if ((moveset == null) || (moveset.size() == 0)) {
            return null;
        }
        int moveCount = moveset.size();
        String[] moves = (String[])moveset.toArray(new String[moveCount]);
        MoveListEntry[] entries = new MoveListEntry[(moveCount >= 4) ? 4 : moveCount];
        Set moveSet = new HashSet();
        int[] ppUp = new int[entries.length];
        for (int i = 0; i < entries.length; ++i) {
            String move;
            do {
                move = moves[random.nextInt(moves.length)];
            } while (moveSet.contains(move));
            moveSet.add(move);
            entries[i] = data.getMoveData().getMove(move);
            ppUp[i] = random.nextInt(4);
        }
        
        String ability = null;
        SortedSet set = species.getPossibleAbilities(speciesData);
        if ((set != null) && (set.size() != 0)) {
            String[] items =
                    (String[])set.toArray(
                        new String[set.size()]);
            ability = items[random.nextInt(items.length)];
        }
        
        set = data.getHoldItemData().getItemSet(species.getName());
        String[] items = (String[])set.toArray(new String[set.size()]);
        String item = items[random.nextInt(items.length)];
        
        int genders = species.getPossibleGenders();
        int gender = GENDER_NONE;
        if (genders != GENDER_NONE) {
            int[] choices = { GENDER_MALE, GENDER_FEMALE };
            while (true) {
                gender = choices[random.nextBoolean() ? 0 : 1];
                if ((genders & gender) != 0)
                    break;
            }
        }
        Pokemon p = new Pokemon(mech, species, nature, ability, item, gender,
                100, ivs, evs, entries, ppUp);
        // Give it a 5% chance of being shiny.
        if (random.nextDouble() < 0.05) {
            p.setShiny(true);
        }
        return p;
    }
    
    /**
     * Load a team from a file and return the ModData used by the team.
     */
    public static ModData loadTeam(File f, Pokemon[] team) {
        ModData modData = null;
        
        try {
            FileInputStream file = new FileInputStream(f);
            ObjectInputStream obj = new ObjectInputStream(file);
            // First thing in file is a UUID identifying the server.
            String uuid = (String)obj.readObject();
            modData = ModData.getModData(uuid);
            if (modData == null) {
                modData = ModData.getDefaultData();
            }
            Pokemon[] pokemon = null;
            synchronized (PokemonSpecies.class) {
                PokemonSpeciesData data = PokemonSpecies.getDefaultData();
                PokemonSpecies.setDefaultData(modData.getSpeciesData());
                try {
                    pokemon = (Pokemon[])obj.readObject();
                } finally {
                    PokemonSpecies.setDefaultData(data);
                }
            }
            if (pokemon != null) {
                System.arraycopy(pokemon, 0, team, 0, team.length);
            }
            obj.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return modData;
    }
    
    /**
     * Set whether this pokemon is shiny.
     */
    public void setShiny(boolean shiny) {
        m_shiny = shiny;
    }
    
    /**
     * Return whether this pokemon is shiny.
     */
    public boolean isShiny() {
        return m_shiny;
    }
    
    /**
     * Return this Pokemon's gender.
     */
    public int getGender() {
        return m_gender;
    }
    
    /**
     * Return the name of this pokemon's ability.
     */
    public String getAbilityName() {
        if ((m_ability == null) || m_ability.isRemovable()) {
            return "";
        }
        return m_ability.getName();
    }
    
    /**
     * Return this pokemon's ability.
     */
    public IntrinsicAbility getAbility() {
        if ((m_ability == null)
                && (m_abilityName != null) &&
                (m_abilityName.length() != 0)) {
            return IntrinsicAbility.getInstance(m_abilityName);
        }
        return m_ability;
    }
    
    /**
     * Set this pokemon's ability.
     * If ignoreTransferability is true then the isTransferrable() method of
     * the ability is ignored. Otherwise it is respected.
     */
    public void setAbility(IntrinsicAbility abl, boolean ignoreTransferability) {
        removeStatus(m_ability);
        if (abl != null) {
            m_abilityName = abl.getName();
            if (ignoreTransferability || abl.isEffectTransferrable()) {
                m_ability = (IntrinsicAbility)addStatus(this, abl);
            } else {
                m_ability = null;
            }
        } else {
            m_abilityName = null;
        }
    }
    
    /**
     * Return the name of this pokemon's item.
     */
    public String getItemName() {
        if ((m_item == null) || m_item.isRemovable()) {
            return "";
        }
        return m_item.getName();
    }
    
    /**
     * Get this pokemon's item.
     */
    public HoldItem getItem() {
        if ((m_item != null) && m_item.isRemovable()) {
            return null;
        }
        return m_item;
    }
    
    /**
     * Set this pokemon's item.
     */
    public void setItem(HoldItem item) {
        removeStatus(m_item);
        if (item != null) {
            m_item = (HoldItem)addStatus(this, item);
        }
        m_itemName = getItemName();
    }
    
    /**
     * Validate this pokemon.
     */
    public void validate(ModData data) throws ValidationException {
        m_mech.validateHiddenStats(this);
        PokemonSpeciesData speciesData = data.getSpeciesData();
        Set set = new HashSet();
        int moveCount = 0;
        for (int i = 0; i < m_move.length; ++i) {
            MoveListEntry move = m_move[i];
            if (move != null) {
                ++moveCount;
                String name = move.getName();
                if (set.contains(name)) {
                    throw new ValidationException(
                            "This pokemon learns two of the same move.");
                }
                set.add(name);
                if (!canLearn(speciesData, name)) {
                    throw new ValidationException("This pokemon cannot learn "
                            + name + ".");
                }
                if ((m_ppUp[i] > 3) || (m_ppUp[i] < 0)) {
                    throw new ValidationException(
                            "Each move must have between zero and "
                            + "three PP ups applied to it.");
                }
            }
        }
        if (moveCount == 0) {
            // Pokemon must have at least one move.
            throw new ValidationException("This pokemon learns no moves.");
        } else if (moveCount > 4) {
            throw new ValidationException(
                    "This pokemon learns move than four moves.");
        }
        
        int genders = getPossibleGenders();
        if (((genders & m_gender) == 0) && ((genders != 0) || (m_gender != 0))) {
            throw new ValidationException("This pokemon has an invalid gender.");
        }
        
        if (!canUseAbility(speciesData, m_abilityName)) {
            SortedSet possibilities = getPossibleAbilities(speciesData);
            if ((possibilities != null) && (possibilities.size() != 0)) {
                m_abilityName = (String)possibilities.first();
            }
        }
        
        if ((m_itemName != null) &&
                !data.getHoldItemData().canUseItem(getSpeciesName(), m_itemName)) {
            throw new ValidationException("This pokemon's item is invalid.");
        }
    }
    
    /**
     * Get the number of PP Ups that have been applied to the given move slot.
     */
    public int getPpUpCount(int i) {
        if ((i < 0) || (i >= m_ppUp.length)) {
            return -1;
        }
        return m_ppUp[i];
    }
    
    /**
     * Calculate stats from a given set of IVs and EVs. The data given are
     * assumed to be valid; no checking is done for illegal values in this
     * function.
     */
    public void calculateStats(int base[], int[] ivs, int[] evs) {
        m_iv = ivs;
        m_ev = evs;
        m_base = base;
        for (int i = 0; i < m_stat.length; ++i) {
            m_stat[i] = m_mech.calculateStat(this, i);
        }
    }
    
    /**
     * Cause this pokemon's stats to be calculated.
     */
    public void calculateStats(boolean resetHp) {
        m_stat = new int[6];
        m_multiplier = new StatMultiplier[m_stat.length];
        for (int i = 0; i < m_stat.length; ++i) {
            m_stat[i] = m_mech.calculateStat(this, i);
            m_multiplier[i] = new StatMultiplier(false);
        }
        if (resetHp) {
            m_hp = m_stat[S_HP];
        }
    }
    
    /**
     * Calculate this pokemon's stats.
     */
    private void initialise() throws StatException {               
        // Recreate transient members.
        m_accuracy = new StatMultiplier(true);
        m_evasion = new StatMultiplier(true);
        m_statuses = Collections.synchronizedList(new ArrayList());
        m_pp = new int[4];
        m_maxPp = new int[m_pp.length];
        m_fainted = false;
        m_field = null;
        m_substitute = 0;
        
        calculateStats(true);
        
        for (int i = 0; i < m_move.length; ++i) {
            if (m_move[i] != null) {
                m_move[i] = (MoveListEntry)m_move[i].clone();
                PokemonMove move = m_move[i].getMove();
                if (move != null) {
                    m_maxPp[i] = m_pp[i] = move.getPp() * (5 + m_ppUp[i]) / 5;
                }
            }
        }
    }
    
    /**
     * Get this pokemon's teammates, including this pokemon.
     */
    public Pokemon[] getTeammates() {
        if (m_field == null) {
            return null;
        }
        return m_field.getParty(m_party);
    }
    
    /**
     * Get the name of this pokemon's trainer.
     */
    public String getTrainerName() {
        if (m_field == null) {
            return null;
        }
        return m_field.getTrainerName(m_party);
    }
    
    /**
     * Get the Pokemon that this Pokemon is fighting in a battle.
     */
    public Pokemon getOpponent() {
        if (m_field == null) {
            return null;
        }
        Pokemon[] active = m_field.getActivePokemon();
        return active[(m_party == 0) ? 1 : 0];
    }
    
    /**
     * Return whether a pokemon is a particular type.
     */
    public boolean isType(PokemonType type) {
        for (int i = 0; i < m_type.length; ++i) {
            if (m_type[i].equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get the (additive) critical hit ability of this Pokemon.
     */
    public int getCriticalHitFactor() {
        return hasAbility("Super Luck") ? 2 : 1;
    }

    /**
     * Return whether this Pokemon is immune to critical hits.
     */
    public boolean isCriticalImmune() {
        return (hasAbility("Battle Armor") || hasAbility("Shell Armor"));
    }
    
    /**
     * Set a move's pp.
     */
    public void setPp(int i, int value) {
        if ((i < 0) || (i >= m_pp.length))
            return;
        m_pp[i] = value;
    }
    
    /**
     * Get a move's pp.
     */
    public int getPp(int i) {
        if ((i < 0) || (i >= m_move.length) || (m_move[i] == null))
            return -1;
        return m_pp[i];
    }
    
    /**
     * Get a move's max pp.
     */
    public int getMaxPp(int i) {
        if ((i < 0) || (i >= m_move.length) || (m_move[i] == null))
            return -1;
        return m_maxPp[i];
    }
    
    /**
     * Get one of this pokemon's moves.
     */
    public MoveListEntry getMove(int i) {
        if (i == -1)
            return BattleField.getStruggle();
        if ((i < -1) || (i >= m_move.length) || (m_move[i] == null))
            return null;
        return m_move[i];
    }
    
    /**
     * This method is called when the pokemon is just about to execute its turn.
     * @param turn the turn that is about to be executed
     */
    public void executeTurn(BattleTurn turn) {
        Iterator i = m_statuses.iterator();
        while (i.hasNext()) {
            StatusEffect j = (StatusEffect)i.next();
            if ((j == null) || !j.isActive()) {
                continue;
            }
            j.executeTurn(this, turn);
        }
    }
    
    /**
     * Return whether it is the pokemon's first turn out.
     */
    public boolean isFirstTurn() {
        return m_firstTurn;
    }
    
    /**
     * Switch in this pokemon.
     */
    public void switchIn() {
        // No iterator - it will freak out if switchIn() adds new statuses.
        m_lastMove = null;
        m_firstTurn = true;
        // Inform PokemonMoves that their potential user is switching in.
        for (int i = 0; i < m_move.length; ++i) {
            MoveListEntry entry = m_move[i];
            if (entry != null) {
                PokemonMove move = entry.getMove();
                if (move != null) {
                    move.switchIn(this);
                }
            }
        }
        // Inform status effects.
        int size = m_statuses.size();
        for (int i = 0; i < size; ++i) {
            ((StatusEffect)m_statuses.get(i)).switchIn(this);
        }
    }
    
    /**
     * Return the original ability of this pokemon.
     */
    public IntrinsicAbility getOriginalAbility() {
        return m_originalAbility;
    }
    
    /**
     * Switch out this pokemon.
     */
    public void switchOut() {
        List list = new ArrayList(m_statuses);
        Iterator i = list.iterator();
        while (i.hasNext()) {
            StatusEffect effect = (StatusEffect)i.next();
            if (effect.isActive() && effect.switchOut(this)) {
                unapplyEffect(effect, false);
                i.remove();
            }
        }
        m_statuses = list;
        setAbility(m_originalAbility, true);
        synchroniseStatuses();
    }
    
    /**
     * Return the effect that vetoes the use of a particular one of this
     * pokemon's moves.
     */
    public StatusEffect getVetoingEffect(int idx) throws MoveQueueException {
        if ((idx < 0) || (idx >= m_move.length)) {
            throw new MoveQueueException("No such move.");
        }
        MoveListEntry entry = m_move[idx];
        if (entry == null) {
            throw new MoveQueueException("No such move.");
        }
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect j = (StatusEffect)i.next();
                if ((j == null) || !j.isActive()) {
                    continue;
                }
                if (j.vetoesMove(this, entry)) {
                    return j;
                }
            }
        }
        return null;
    }
    
    /**
     * Return whether this pokemon has a particular effect.
     */
    public boolean hasEffect(StatusEffect eff) {
        if (eff == null) {
            return false;
        }
        Iterator i = m_statuses.iterator();
        while (i.hasNext()) {
            StatusEffect j = (StatusEffect)i.next();
            if ((j == null) || !j.isActive()) {
                continue;
            }
            if (eff.equals(j)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return whether this Pokemon has a particular class of effect.
     */
    public boolean hasEffect(int lock) {
        return (getEffect(lock) != null);
    }
    
    /**
     * Return the effect applied to this pokemon of a particular lock or
     * null if there is no such effect applied.
     */
    public StatusEffect getEffect(int lock) {
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect eff = (StatusEffect)i.next();
                if ((eff == null) || !eff.isActive()) {
                    continue;
                }
                if (eff.getLock() == lock) {
                    return eff;
                }
            }
        }
        return null;
    }
    
    /**
     * Return the effect of a particular class applied to this pokemon, or
     * null if there is no such effect.
     */
    public StatusEffect getEffect(Class type) {
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect eff = (StatusEffect)i.next();
                if ((eff == null) || !eff.isActive()) {
                    continue;
                }
                if (type.isAssignableFrom(eff.getClass())) {
                    return eff;
                }
            }
        }
        return null;
    }
    
    /**
     * Return whether this Pokemon has a particular class of effect.
     */
    public boolean hasEffect(Class type) {
        return (getEffect(type) != null);
    }
    
    /**
     * Return whether this pokemon has a particular ability.
     */
    public boolean hasAbility(String name) {
        if (m_ability == null) {
            return false;
        }
        return (m_ability.isActive() && m_ability.getName().equals(name));
    }
    
    /**
     * Return whether this pokemon has a particular item.
     */
    public boolean hasItem(String name) {
        if (m_item == null) {
            return false;
        }
        return (m_item.isActive() && m_item.getName().equals(name));
    }
    
    /**
     * Return whether this pokemon is active (able to choose moves and switch).
     */
    public boolean isActive() {
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect eff = (StatusEffect)i.next();
                if (eff.isActive() && eff.deactivates(this)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Get the name of this species.
     */
    public String getSpeciesName() {
        return super.getName();
    }
    
    /**
     * Set this pokemon's name.
     */
    public void setName(String name) {
        m_nickname = name;
    }
    
    /**
     * Get the display name of this pokemon (i.e. its nickname).
     */
    public String getName() {
        return m_nickname;
    }
    
    /**
     * Get all status effects of a certain tier.
     */
    public List getStatusesByTier(int tier) {
        List ret = new ArrayList();
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect effect = (StatusEffect)i.next();
                if (effect.isActive() && (effect.getTier() == tier)) {
                    ret.add(effect);
                }
            }
        }
        return ret;
    }
    
    /**
     * Get a list of statuses that are not special, weather, abilities, or
     * items.
     * @param lock status lock to allow
     */
    public List getNormalStatuses(int lock) {
        List ret = new ArrayList();
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect effect = (StatusEffect)i.next();
                if (!effect.isActive()) continue;
                // Note: HoldItem is a subclass of IntrinsicAbility.
                if (!(effect instanceof IntrinsicAbility)) {
                    int effLock = effect.getLock();
                    if ((effLock == 0) || (effLock == lock)) {
                        ret.add(effect);
                    }
                }
            }
        }
        return ret;
    }
    
    /**
     * Return whether this pokemon can switch.
     */
    public boolean canSwitch() {
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect effect = (StatusEffect)i.next();
                if (effect.isActive() && !effect.canSwitch(this)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Begin ticking effects.
     */
    public void beginStatusTicks() {
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect effect = (StatusEffect)i.next();
                effect.beginTick();
            }
        }
    }
    
    /**
     * Remove status effects that have ended.
     */
    public void synchroniseStatuses() {
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect effect = (StatusEffect)i.next();
                if (effect.isRemovable()) {
                    i.remove();
                }
            }
        }
    }
    
    /**
     * Invoke unapply on a status effect, optionally disabling it.
     */
    private void unapplyEffect(StatusEffect eff, boolean disable) {
        if (eff.isActive()) {
            eff.unapply(this);
        }
        if (disable) {
            eff.disable();
        }
        if (m_field != null) {
            m_field.informStatusRemoved(this, eff);
        }
        informStatusListeners(null, eff, false);
    }
    
    /**
     * Invoke unapply on a status effect and disable it as well.
     */
    private void unapplyEffect(StatusEffect eff) {
        unapplyEffect(eff, true);
    }
    
    /**
     * Remove a status effect from this pokemon.
     */
    public void removeStatus(StatusEffect eff) {
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect effect = (StatusEffect)i.next();
                if (effect == eff) {
                    unapplyEffect(eff);
                    return;
                }
            }
        }
    }
    
    /**
     * Remove a class of statuses from this pokemon.
     */
    public void removeStatus(int lock) {
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect effect = (StatusEffect)i.next();
                if ((effect.getLock() == lock) && !effect.isRemovable()) {
                    unapplyEffect(effect);
                }
            }
        }
    }
    
    /**
     * Remove statuses by class type.
     */
    public void removeStatus(Class type) {
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect effect = (StatusEffect)i.next();
                if (!effect.isRemovable() && type.isAssignableFrom(effect.getClass())) {
                    unapplyEffect(effect);
                }
            }
        }
    }
    
    /**
     * Attach this pokemon to a battle field.
     */
    public void attachToField(BattleField field, int party, int position) {
        m_field = field;
        m_mech = m_field.getMechanics();
        m_party = party;
        m_id = position;
        
        if ((m_abilityName != null) && (m_abilityName.length() != 0)) {
            m_originalAbility = IntrinsicAbility.getInstance(m_abilityName);
            if (m_originalAbility != null) {
                m_ability = (IntrinsicAbility)addStatus(this, m_originalAbility);
            }
        }
        
        if ((m_itemName != null) && (m_itemName.length() != 0)) {
            IntrinsicAbility item = IntrinsicAbility.getInstance(m_itemName);
            if ((item != null) && (item instanceof HoldItem)) {
                m_item = (HoldItem)addStatus(this, item);
            }
        }
    }
    
    /**
     * Get this pokemon's party.
     * 
     * This will be in the range [0, <b>parties</b> - 1] where <b>parties</b>
     * is the number of parties on the battle field (probably two).
     */
    public int getParty() {
        return m_party;
    }
    
    /**
     * Get this pokemon's position on the field to which it is attached.
     */
    public int getId() {
        return m_id;
    }
    
    /**
     * Get the field to which this pokemon is attached.
     */
    public BattleField getField() {
        return m_field;
    }
    
    /**
     * Get the name of this pokemon's moves.
     */
    public String getMoveName(int i) {
        if (!(i < m_move.length) || (m_move[i] == null)) {
            return null;
        }
        return m_move[i].getName();
    }
    
    /**
     * Determine whether this pokemon has fainted.
     */
    public boolean isFainted() {
        return m_fainted;
    }
    
    /**
     * Get the effectiveness of this pokemon attacking a particular type.
     */
    public static double getEffectiveness(List statuses, PokemonType move,
            PokemonType pokemon, boolean enemy) {
        double expected = move.getMultiplier(pokemon);
        synchronized (statuses) {
            Iterator i = statuses.iterator();
            while (i.hasNext()) {
                StatusEffect eff = (StatusEffect)i.next();
                if (eff.isActive() && eff.isEffectivenessTransformer(enemy)) {
                    double actual = eff.getEffectiveness(move, pokemon, enemy);
                    if (actual != expected)
                        return actual;
                }
            }
        }
        return expected;
    }
    
    public double getEffectiveness(PokemonType move, PokemonType pokemon,
            boolean enemy) {
        return getEffectiveness(m_statuses, move, pokemon, enemy);
    }

    /**
     * Is this pokemon immobilised?
     * @param exception status not to check for
     */
    public boolean isImmobilised(Class exception) {
        synchronized (m_statuses) {
            Collections.sort(m_statuses, new Comparator() {
                public int compare(Object o1, Object o2) {
                    StatusEffect e1 = (StatusEffect)o1;
                    StatusEffect e2 = (StatusEffect)o2;
                    return e1.getTier() - e2.getTier();
                }
            });
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect eff = (StatusEffect)i.next();
                if (eff.isActive() && eff.immobilises(this)) {
                    if ((exception == null) || !exception.isAssignableFrom(eff.getClass())) {
                        m_lastMove = null;
                        m_firstTurn = false;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Transform a move based on the status effects applied to the pokemon.
     * @param enemy whether this Pokemon is an enemy
     */
    protected MoveListEntry getTransformedMove(MoveListEntry move, boolean enemy) {
        // For now, do this in no particular order.
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect eff = (StatusEffect)i.next();
                if (eff.isActive() && eff.isMoveTransformer(enemy)) {
                    move = eff.getMove(this, (MoveListEntry)move.clone(), enemy);
                    if (move == null) {
                        return null;
                    }
                }
            }
        }
        return move;
    }
    
    /**
     * Get the last move used by this pokemon, or null if the pokemon has not
     * used a move since it has been out.
     */
    public MoveListEntry getLastMove() {
        return m_lastMove;
    }
    
    /**
     * Use one of this pokemon's intrinsic moves.
     */
    public int useMove(int i, Pokemon target) {
        if (i == -1) {
            MoveListEntry move = BattleField.getStruggle();
            int ret = useMove(move, target);
            m_lastMove = move;
            m_firstTurn = false;
            return ret;
        }
        if ((i >= m_move.length) || (m_move[i] == null))
            return 0;
        if (m_pp[i] == 0)
            return 0;

        MoveListEntry entry = m_move[i];
        PokemonMove move = m_move[i].getMove();
        
        final int cost = (target.hasAbility("Pressure") && move.isAttack()) ? 2 : 1;
        m_pp[i] -= cost;
        if (m_pp[i] < 0) m_pp[i] = 0;
        
        int ret = useMove(entry, target);
        m_lastMove = entry;
        m_firstTurn = false;
        return ret;
    }
    
    /**
     * Use a a move from the move list (so its name is known and displayed).
     */
    public int useMove(MoveListEntry move, Pokemon target) {
        PokemonMove pmove = move.getMove();
        move = getTransformedMove(move, false);
        if (move != null) {
            if (target != this) {
                if ((move = target.getTransformedMove(move, true)) == null) {
                    return 0;
                }
            }
            m_field.informUseMove(this, move.getName());
            int hp = target.getHealth();
            pmove = move.getMove();
            useMove(pmove, target);
            int damage = hp - target.getHealth(); 
            if (damage > 0) {
                target.informDamaged(this, move, damage);
            }
            return damage;
        }
        return 0;
    }
    
    /**
     * Check for accuracy and then use an arbitrary move.
     */
    public int useMove(PokemonMove move, Pokemon target) {
        if (!move.attemptHit(m_mech, this, target)) {
            return 0;
        }
        return move.use(m_mech, this, target);
    }
    
    /**
     * Inform that this pokemon was damaged.
     */
    private void informDamaged(Pokemon source, MoveListEntry entry, int damage) {
        int size = m_statuses.size();
        for (int i = 0; i < size; ++i) {
            StatusEffect eff = (StatusEffect)m_statuses.get(i);
            if (eff.isActive() && eff.isListener()) {
                eff.informDamaged(source, this, entry, damage);
            }
        }
    }
    
    /**
     * Change the health of this pokemon, doing damage to a substitute if one
     * is present.
     */
    public void changeHealth(int hp) {
        changeHealth(hp, false);
    }
    
    /**
     * Change the health of this pokemon, optionally hitting through a
     * substitute.
     */
    public void changeHealth(int hp, boolean throughSubstitute) {
        if (m_fainted)
            return;
        if (!hasSubstitute() || throughSubstitute || (hp > 0)) {
            if (throughSubstitute && (hp < 0) && hasAbility("Magic Guard")) 
                return;
            int max = m_stat[S_HP];
            int display = hp;
            int result = m_hp + hp;
            if (hasEffect(PendanticDamageClause.class)) {
                if (result > max) {
                    display = max - m_hp;
                } else if (result < 0) {
                    display = -m_hp;
                }
            }
            m_field.informPokemonHealthChanged(this, display);
            if ((result <= 0) && !throughSubstitute) {
                boolean live = false;
                if (hasEffect(MoveList.EndureEffect.class)) {
                    m_field.showMessage(getName() + " endured the attack!");
                    live = true;
                } else if ((m_hp == max) && hasItem("Focus Sash")) {
                    m_field.showMessage(getName() + " hung on using its Focus Sash!");
                    live = true;
                    setItem(null);
                } else if (hasItem("Focus Band")) {
                    if (m_field.getRandom().nextDouble() <= 0.1) {
                        m_field.showMessage(getName() + " hung on using its Focus Band!");
                        live = true;
                    }
                }
                if (live) {
                    hp = -m_hp + 1;
                }
            }
            m_hp += hp;
            if (m_hp <= 0) {
                faint();
            } else if (m_hp > max) {
                m_hp = max;
            }
        } else {
            m_substitute += hp;
            String name = getName();
            m_field.showMessage("The substitute took damage for " + name + "!");
            if (m_substitute <= 0) {
                m_field.showMessage(name + "'s substitute faded!");
                m_substitute = 0;
                removeStatus(MoveList.SubstituteEffect.class);
            }
        }
    }
    
    /**
     * Cause this pokemon to faint.
     */
    public void faint() {
        m_hp = 0;
        m_fainted = true;
        if (m_field != null) {
            m_field.informPokemonFainted(m_party, getId());
            m_field.checkBattleEnd(m_party);
        }
    }
    
    /**
     * Get the health of this pokemon.
     */
    public int getHealth() {
        return m_hp;
    }
    
    /**
     * Inform listeners that a status effect was applied this pokemon.
     */
    private void informStatusListeners(Pokemon source, StatusEffect eff, boolean applied) {
        synchronized (m_statuses) {
            int size = m_statuses.size();
            for (int i = 0; i < size; ++i) {
                StatusEffect j = (StatusEffect)m_statuses.get(i);
                if (j.isActive() && (j instanceof StatusListener)) {
                    StatusListener k = (StatusListener)j;
                    if (applied) {
                        k.informStatusApplied(source, this, eff);
                    } else {
                        k.informStatusRemoved(this, eff);
                    }
                }
            }
        }
    }
    
    /**
     * Return whether this pokemon must struggle if it wants to use a move.
     */
    public boolean mustStruggle() {
        for (int i = 0; i < m_move.length; ++i) {
            try {
                if (getVetoingEffect(i) != null) {
                    continue;
                }
            } catch (MoveQueueException e) {
                continue;
            }
            if (getPp(i) > 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check whether the effects present on this pokemon permit the
     * application of the given status effect to this pokemon.
     */
    public boolean allowsStatus(StatusEffect eff, Pokemon source) {
        Iterator i = m_statuses.iterator();
        while (i.hasNext()) {
            StatusEffect clause = (StatusEffect)i.next();
            if ((clause == null) || !clause.isActive())
                continue;
            if (!clause.allowsStatus(eff, source, this))
                return false;
        }
        return true;
    }
    
    /**
     * Add a status effect to this pokemon.
     */
    public StatusEffect addStatus(Pokemon source, StatusEffect eff) {
        if (m_fainted)
            return null;
        
        // Make sure there isn't another copy of this effect applied already.
        synchronized (m_statuses) {
            Iterator i = m_statuses.iterator();
            while (i.hasNext()) {
                StatusEffect j = (StatusEffect)i.next();
                if (!j.isRemovable() && (eff.equals(j) || eff.isExclusiveWith(j))) {
                    return null;
                }
            }
        }
        
        StatusEffect applied = (StatusEffect)eff.clone();
        applied.activate();
        applied.setInducer(source);
        if ((m_field != null) && !allowsStatus(applied, source))
            return null;
        
        if (applied.apply(this)) {
            m_statuses.add(applied);
            if (m_field != null) {
                m_field.informStatusApplied(this, applied);
            }
            informStatusListeners(source, applied, true);
        }
        return applied;
    }
    
    public PokemonNature getNature() {
        return m_nature;
    }
    
    public StatMultiplier getAccuracy() {
        return m_accuracy;
    }
    
    public StatMultiplier getEvasion() {
        return m_evasion;
    }
    
    public void setLevel(int level) {
        m_level = level;
    }
    
    public int getLevel() {
        return m_level;
    }
    
    public int getIv(int i) throws StatException {
        if ((i < 0) || (i > 5)) throw new StatException();
        return m_iv[i];
    }
    
    public int getEv(int i) throws StatException {
        if ((i < 0) || (i > 5)) throw new StatException();
        return m_ev[i];
    }

    /**
     * Get a stat multiplier, including the ones for accuracy and evasion.
     */
    public StatMultiplier getMultiplier(int i) throws StatException {
        if (i < 0)
            throw new StatException();
        if (i < 6)
            return m_multiplier[i];
        if (i == S_ACCURACY)
            return m_accuracy;
        if (i == S_EVASION)
            return m_evasion;
        throw new StatException();
    }
    
    public int getRawStat(int i) {
        if ((i < 0) || (i > 5)) throw new StatException();
        return m_stat[i];
    }
    
    public void setRawStat(int i, int newStat) {
        if ((i < 0) || (i > 5)) throw new StatException();
        m_stat[i] = newStat;
    }
    
    public int getStat(int i, double multiplier) {
        if ((i < 0) || (i > 5)) throw new StatException();
        return (int)(((double)m_stat[i]) * multiplier);
    }
    
    public int getStat(int i) {
        if ((i < 0) || (i > 5)) throw new StatException();
        // Consider stat modifications.
        return getStat(i, m_multiplier[i].getMultiplier());
    }
    
}
