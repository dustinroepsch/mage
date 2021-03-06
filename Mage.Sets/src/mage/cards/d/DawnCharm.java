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
package mage.cards.d;

import mage.abilities.Mode;
import mage.abilities.effects.common.CounterTargetEffect;
import mage.abilities.effects.common.PreventAllDamageByAllPermanentsEffect;
import mage.abilities.effects.common.RegenerateTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.filter.FilterSpell;
import mage.filter.predicate.ObjectPlayer;
import mage.filter.predicate.ObjectPlayerPredicate;
import mage.game.Game;
import mage.game.stack.StackObject;
import mage.target.Target;
import mage.target.TargetSpell;
import mage.target.common.TargetCreaturePermanent;

import java.util.UUID;

/**
 *
 * @author LevelX2
 */
public class DawnCharm extends CardImpl {

    private static final FilterSpell filter = new FilterSpell("spell that targets you");

    static {
        filter.add(new DawnCharmPredicate());
    }

    public DawnCharm(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{1}{W}");


        // Choose one - Prevent all combat damage that would be dealt this turn
        this.getSpellAbility().addEffect(new PreventAllDamageByAllPermanentsEffect(Duration.EndOfTurn, true));
        // or regenerate target creature;
        Mode mode = new Mode();
        mode.getEffects().add(new RegenerateTargetEffect());
        mode.getTargets().add(new TargetCreaturePermanent());
        this.getSpellAbility().addMode(mode);
        // or counter target spell that targets you.
        mode = new Mode();
        mode.getEffects().add(new CounterTargetEffect());
        mode.getTargets().add(new TargetSpell(filter));
        this.getSpellAbility().addMode(mode);
    }

    public DawnCharm(final DawnCharm card) {
        super(card);
    }

    @Override
    public DawnCharm copy() {
        return new DawnCharm(this);
    }
}

class DawnCharmPredicate implements ObjectPlayerPredicate<ObjectPlayer<StackObject>> {

    @Override
    public boolean apply(ObjectPlayer<StackObject> input, Game game) {
        UUID controllerId = input.getPlayerId();
        if (controllerId == null) {
            return false;
        }

        for (Target target : input.getObject().getStackAbility().getTargets()) {
            for (UUID targetId : target.getTargets()) {
                if (controllerId.equals(targetId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "spell that targets you";
    }
}
