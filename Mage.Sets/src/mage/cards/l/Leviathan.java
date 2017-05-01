/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.l;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.BeginningOfUpkeepTriggeredAbility;
import mage.abilities.common.EntersBattlefieldTappedAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.common.SacrificeAllCost;
import mage.abilities.costs.common.SacrificeTargetCost;
import mage.abilities.costs.common.SacrificeXTargetCost;
import mage.abilities.effects.PayCostToAttackBlockEffectImpl;
import mage.abilities.effects.common.*;
import mage.abilities.keyword.TrampleAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.filter.common.FilterControlledLandPermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.target.common.TargetControlledPermanent;

import java.util.UUID;

/**
 * @author dustinroepsch
 */
public class Leviathan extends CardImpl {
    private static final FilterControlledLandPermanent filter = new FilterControlledLandPermanent("two Islands");

    static {
        filter.add(new SubtypePredicate("Island"));
    }

    public Leviathan(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{5}{U}{U}{U}{U}");

        this.subtype.add("Leviathan");
        this.power = new MageInt(10);
        this.toughness = new MageInt(10);

        // Trample
        this.addAbility(TrampleAbility.getInstance());

        // Leviathan enters the battlefield tapped and doesn't untap during your untap step.
        this.addAbility(new EntersBattlefieldTappedAbility());
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new DontUntapInControllersUntapStepSourceEffect()));

        // At the beginning of your upkeep, you may sacrifice two Islands. If you do, untap Leviathan.
        this.addAbility(new BeginningOfUpkeepTriggeredAbility(
                Zone.BATTLEFIELD,
                new DoIfCostPaid(new UntapSourceEffect(), new SacrificeTargetCost(new TargetControlledPermanent(2, 2, filter, true))),
                TargetController.YOU,
                true
        ));


        // Leviathan can't attack unless you sacrifice two Islands.
        this.addAbility(new SimpleStaticAbility(
                Zone.BATTLEFIELD,
                new LeviathanCostToAttackEffect()
        ));
    }

    public Leviathan(final Leviathan card) {
        super(card);
    }

    @Override
    public Leviathan copy() {
        return new Leviathan(this);
    }
}

class LeviathanCostToAttackEffect extends PayCostToAttackBlockEffectImpl {
    private static final FilterControlledLandPermanent filter = new FilterControlledLandPermanent("two Islands");

    static {
        filter.add(new SubtypePredicate("Island"));
    }

    LeviathanCostToAttackEffect() {
        super(Duration.WhileOnBattlefield, Outcome.Detriment, RestrictType.ATTACK,
                new SacrificeTargetCost(new TargetControlledPermanent(2, 2, filter, true))
        );
    }

    public LeviathanCostToAttackEffect(LeviathanCostToAttackEffect effect) {
        super(effect);
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        return source.getSourceId().equals(event.getSourceId());
    }

    @Override
    public LeviathanCostToAttackEffect copy() {
        return new LeviathanCostToAttackEffect(this);
    }
}
