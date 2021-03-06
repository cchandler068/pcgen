/*
 * Copyright (c) 2007 Tom Parker <thpr@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package plugin.lsttokens.editcontext.spell;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import pcgen.cdom.base.Constants;
import pcgen.cdom.list.ClassSpellList;
import pcgen.core.spell.Spell;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.editcontext.testsupport.AbstractIntegrationTestCase;
import plugin.lsttokens.editcontext.testsupport.TestContext;
import plugin.lsttokens.spell.ClassesToken;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.TokenRegistration;
import plugin.pretokens.parser.PreRaceParser;
import plugin.pretokens.writer.PreRaceWriter;

public class ClassesIntegrationTest extends AbstractIntegrationTestCase<Spell>
{

	static ClassesToken token = new ClassesToken();
	static CDOMTokenLoader<Spell> loader = new CDOMTokenLoader<Spell>(
			Spell.class);

	PreRaceParser prerace = new PreRaceParser();
	PreRaceWriter preracewriter = new PreRaceWriter();

	@Override
	@Before
	public void setUp() throws PersistenceLayerException, URISyntaxException
	{
		super.setUp();
		TokenRegistration.register(prerace);
		TokenRegistration.register(preracewriter);
	}

	@Override
	public Class<Spell> getCDOMClass()
	{
		return Spell.class;
	}

	@Override
	public CDOMLoader<Spell> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<Spell> getToken()
	{
		return token;
	}

	@Test
	public void testRoundRobinSimple() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class, "Wizard");
		primaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Sorcerer");
		primaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class, "Cleric");
		secondaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Wizard");
		secondaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class,
				"Sorcerer");
		secondaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Cleric");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "Wizard=4");
		commit(modCampaign, tc, "Sorcerer=3|Cleric=4");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinRemovePre() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class, "Wizard");
		secondaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Wizard");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "Wizard=4[PRERACE:1,Human]");
		commit(modCampaign, tc, "Wizard=4");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinAddPre() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class, "Wizard");
		secondaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Wizard");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "Wizard=4");
		commit(modCampaign, tc, "Wizard=4[PRERACE:1,Human]");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinTestMinus() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class, "Wizard");
		secondaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Wizard");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "Wizard=-1");
		commit(modCampaign, tc, "Wizard=4");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinModMinus() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class, "Wizard");
		secondaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Wizard");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "Wizard=4");
		commit(modCampaign, tc, "Wizard=-1");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinNoSet() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Sorcerer");
		secondaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class,
				"Sorcerer");
		TestContext tc = new TestContext();
		emptyCommit(testCampaign, tc);
		commit(modCampaign, tc, "Sorcerer=3");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinNoReset() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class, "Wizard");
		secondaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Wizard");
		primaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Sorcerer");
		secondaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class,
				"Sorcerer");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "Sorcerer,Wizard=2");
		emptyCommit(modCampaign, tc);
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinAll() throws PersistenceLayerException
	{
		verifyCleanStart();
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "ALL=2");
		emptyCommit(modCampaign, tc);
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinAllClear() throws PersistenceLayerException
	{
		verifyCleanStart();
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "ALL=2");
		commit(modCampaign, tc, Constants.LST_DOT_CLEAR_ALL);
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinAllSupplement() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Sorcerer");
		secondaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class,
				"Sorcerer");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "ALL=2");
		commit(modCampaign, tc, "Sorcerer=1");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinClearEmtpy() throws PersistenceLayerException
	{
		verifyCleanStart();
		TestContext tc = new TestContext();
		commit(testCampaign, tc, Constants.LST_DOT_CLEAR_ALL);
		emptyCommit(modCampaign, tc);
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinEmptyClear() throws PersistenceLayerException
	{
		verifyCleanStart();
		TestContext tc = new TestContext();
		emptyCommit(testCampaign, tc);
		commit(modCampaign, tc, Constants.LST_DOT_CLEAR_ALL);
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinAllClearMinus() throws PersistenceLayerException
	{
		verifyCleanStart();
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "ALL=2");
		commit(modCampaign, tc, "ALL=-1");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinAllSupplementMinus() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Sorcerer");
		secondaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class,
				"Sorcerer");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "Sorcerer=-1");
		commit(modCampaign, tc, "Sorcerer=1");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinAllSupplementMinusMod() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Sorcerer");
		secondaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class,
				"Sorcerer");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "Sorcerer=1");
		commit(modCampaign, tc, "Sorcerer=-1");
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinClearEmtpyMinus()
			throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Sorcerer");
		secondaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class,
				"Sorcerer");
		TestContext tc = new TestContext();
		commit(testCampaign, tc, "Sorcerer=-1");
		emptyCommit(modCampaign, tc);
		completeRoundRobin(tc);
	}

	@Test
	public void testRoundRobinEmptyCleaMinus() throws PersistenceLayerException
	{
		verifyCleanStart();
		primaryContext.getReferenceContext()
				.constructCDOMObject(ClassSpellList.class, "Sorcerer");
		secondaryContext.getReferenceContext().constructCDOMObject(ClassSpellList.class,
				"Sorcerer");
		TestContext tc = new TestContext();
		emptyCommit(testCampaign, tc);
		commit(modCampaign, tc, "Sorcerer=-1");
		completeRoundRobin(tc);
	}

}
