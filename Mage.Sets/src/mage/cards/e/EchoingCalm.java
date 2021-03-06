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
package mage.cards.e;

import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Outcome;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;
import mage.target.common.TargetEnchantmentPermanent;

import java.util.UUID;

/**
 * @author Loki
 */
public class EchoingCalm extends CardImpl {

    public EchoingCalm(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.INSTANT},"{1}{W}");


        // Destroy target enchantment and all other enchantments with the same name as that enchantment.
        this.getSpellAbility().addTarget(new TargetEnchantmentPermanent());
        this.getSpellAbility().addEffect(new EchoingCalmEffect());
    }

    public EchoingCalm(final EchoingCalm card) {
        super(card);
    }

    @Override
    public EchoingCalm copy() {
        return new EchoingCalm(this);
    }
}

class EchoingCalmEffect extends OneShotEffect {
    EchoingCalmEffect() {
        super(Outcome.DestroyPermanent);
        staticText = "Destroy target enchantment and all other enchantments with the same name as that enchantment";
    }

    EchoingCalmEffect(final EchoingCalmEffect effect) {
        super(effect);
    }

    @Override
    public EchoingCalmEffect copy() {
        return new EchoingCalmEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        Permanent permanent = game.getPermanent(getTargetPointer().getFirst(game, source));
        if (controller != null && permanent != null) {
            permanent.destroy(source.getSourceId(), game, false);
            if (!permanent.getName().isEmpty()) { // in case of face down enchantment creature
                for (Permanent perm : game.getBattlefield().getActivePermanents(source.getControllerId(), game)) {
                    if (!perm.getId().equals(permanent.getId()) && perm.getName().equals(permanent.getName()) && perm.isEnchantment()) {
                        perm.destroy(source.getSourceId(), game, false);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
