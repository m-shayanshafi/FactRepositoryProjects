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
package pl.org.minions.stigma.client.ui.swing.game.components.items;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.org.minions.stigma.client.ui.VisualizationGlobals;
import pl.org.minions.stigma.client.ui.swing.game.components.ValueIconLabel;
import pl.org.minions.stigma.game.actor.Actor;
import pl.org.minions.stigma.game.actor.AttackType;
import pl.org.minions.stigma.game.actor.DamageType;
import pl.org.minions.stigma.game.item.type.WeaponAttack;
import pl.org.minions.stigma.globals.TurnCalc;
import pl.org.minions.utils.i18n.Translated;

/**
 * Panel representing attack description.
 */
public class WeaponAttackPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 80;

    @Translated(name = "AttackFire")
    private static String FIRE_STR = "Fire";
    @Translated(name = "AttackThrust")
    private static String THRUST_STR = "Thrust";
    @Translated(name = "AttackSwing")
    private static String SWING_STR = "Swing";
    @Translated(name = "AttackThrow")
    private static String THROW_STR = "Throw";

    @Translated
    private static String MELEE_STR = "Melee";

    private static EnumMap<AttackType, String> NAME_MAP =
            new EnumMap<AttackType, String>(AttackType.class);
    private static EnumMap<DamageType, BufferedImage> IMG_MAP =
            new EnumMap<DamageType, BufferedImage>(DamageType.class);

    static
    {
        NAME_MAP.put(AttackType.FIRE, FIRE_STR);
        NAME_MAP.put(AttackType.THRUST, THRUST_STR);
        NAME_MAP.put(AttackType.SWING, SWING_STR);
        NAME_MAP.put(AttackType.THROW, THROW_STR);

        IMG_MAP.put(DamageType.CUTTING, VisualizationGlobals.CUTTING_IMG);
        IMG_MAP.put(DamageType.BLUNT, VisualizationGlobals.BLUNT_IMG);
        IMG_MAP.put(DamageType.PIERCING, VisualizationGlobals.PIERCING_IMG);
        IMG_MAP.put(DamageType.FROSTBITE, VisualizationGlobals.FROSTBITE_IMG);
        IMG_MAP.put(DamageType.BURN, VisualizationGlobals.BURN_IMG);
        IMG_MAP.put(DamageType.SHOCK, VisualizationGlobals.SHOCK_IMG);
        IMG_MAP.put(DamageType.POISON, VisualizationGlobals.POISON_IMG);
    }

    private JLabel attackTypeLabel;
    private JLabel rangeLabel;
    private ValueIconLabel attackLabel;
    private ValueIconLabel damageLabel;
    private ValueIconLabel criticalLabel;
    private ValueIconLabel cooldownLabel;

    /**
     * Constructor.
     */
    public WeaponAttackPanel()
    {
        initialize();
    }

    /**
     * Sets panel contents accordng to given values.
     * @param type
     *            type of attack to rtepresent
     * @param attack
     *            attack description
     * @param actor
     *            player performing attack
     */
    public void setValue(AttackType type, WeaponAttack attack, Actor actor)
    {
        attackTypeLabel.setText(NAME_MAP.get(type));
        if (attack.getRange() > 1)
            rangeLabel.setText(attack.getRange() + " m");
        else
            rangeLabel.setText(MELEE_STR);
        attackLabel.setText(String.valueOf(attack.getAttack()));

        damageLabel.setText(calculateAttackDamageValue(attack, actor));
        damageLabel.setIcon(IMG_MAP.get(attack.getDamageType()));

        criticalLabel.setText(attack.getCriticalChance() + "%");

        cooldownLabel.setText(TurnCalc.turns2secondsStr(attack.getCooldown())
            + " s");
    }

    /**
     * Calculates attack value of weapon for selected attack
     * type
     * @return String representing attack type
     */
    private String calculateAttackDamageValue(WeaponAttack wa, Actor player)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(wa.getBaseDamage());

        sb.append(" (");

        int calculatedDamage = wa.getBaseDamage();
        int statisticValue = 0;

        if (player.getAgility() >= wa.getAgilityBonusMin()
            && wa.getAgilityBonusStep() != 0)
        {
            statisticValue =
                    player.getAgility() <= wa.getAgilityBonusMax() ? player.getAgility()
                                                                  : wa.getAgilityBonusMax();

            calculatedDamage +=
                    (int) Math.floor(statisticValue / wa.getAgilityBonusStep());
        }

        if (player.getFinesse() >= wa.getFinesseBonusMin()
            && wa.getFinesseBonusStep() != 0)
        {
            statisticValue =
                    player.getFinesse() <= wa.getFinesseBonusMax() ? player.getFinesse()
                                                                  : wa.getFinesseBonusMax();

            calculatedDamage +=
                    (int) Math.floor(statisticValue / wa.getFinesseBonusStep());
        }

        if (player.getStrength() >= wa.getStrenghtBonusMin()
            && wa.getStrenghtBonusStep() != 0)
        {
            statisticValue =
                    player.getStrength() <= wa.getStrenghtBonusMax() ? player.getStrength()
                                                                    : wa.getStrenghtBonusMax();

            calculatedDamage +=
                    (int) Math.floor(statisticValue / wa.getStrenghtBonusStep());
        }

        if (player.getWillpower() >= wa.getWillpowerBonusMin()
            && wa.getWillpowerBonusStep() != 0)
        {
            statisticValue =
                    player.getWillpower() <= wa.getWillpowerBonusMax() ? player.getWillpower()
                                                                      : wa.getWillpowerBonusMax();

            calculatedDamage +=
                    (int) Math.floor(statisticValue
                        / wa.getWillpowerBonusStep());
        }
        sb.append(calculatedDamage);
        sb.append(")");
        return sb.toString();
    }

    private void initialize()
    {
        setMinimumSize(new Dimension(WIDTH, 0));

        attackTypeLabel = new JLabel();
        attackTypeLabel.setText("Type");
        Box attackTypeBox = Box.createHorizontalBox();
        attackTypeBox.add(attackTypeLabel);
        attackTypeBox.add(Box.createHorizontalGlue());

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(attackTypeBox);
        this.add(getPanel());
        this.add(Box.createVerticalGlue());
    }

    private Box getPanel()
    {
        // CHECKSTYLE:OFF
        Box panel = Box.createHorizontalBox();

        panel.add(Box.createHorizontalStrut(25));
        panel.add(getInnerPanel());
        panel.add(Box.createHorizontalGlue());
        return panel;
        // CHEKCSTYLE:ON
    }

    private Box getInnerPanel()
    {
        Box panel = Box.createVerticalBox();

        rangeLabel = new JLabel();
        rangeLabel.setText("range");
        rangeLabel.setAlignmentX(LEFT_ALIGNMENT);

        attackLabel = new ValueIconLabel();
        attackLabel.setText("attack");
        attackLabel.setIcon(VisualizationGlobals.ATTACK_IMG);
        attackLabel.setAlignmentX(LEFT_ALIGNMENT);

        damageLabel = new ValueIconLabel();
        damageLabel.setText("damage");
        damageLabel.setAlignmentX(LEFT_ALIGNMENT);

        criticalLabel = new ValueIconLabel();
        criticalLabel.setText("critical");
        criticalLabel.setIcon(VisualizationGlobals.CRITICAL_IMG);
        criticalLabel.setAlignmentX(LEFT_ALIGNMENT);

        cooldownLabel = new ValueIconLabel();
        cooldownLabel.setText("cooldown");
        cooldownLabel.setIcon(VisualizationGlobals.COOLDOWN_IMG);
        cooldownLabel.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(rangeLabel);
        panel.add(attackLabel);
        panel.add(damageLabel);
        panel.add(criticalLabel);
        panel.add(cooldownLabel);
        panel.add(Box.createHorizontalGlue());
        return panel;
    }
}
