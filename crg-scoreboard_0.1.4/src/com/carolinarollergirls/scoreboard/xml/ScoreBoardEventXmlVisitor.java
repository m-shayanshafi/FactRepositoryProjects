package com.carolinarollergirls.scoreboard.xml;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.defaults.*;
import com.carolinarollergirls.scoreboard.event.*;

/**
 * Converts a ScoreBoardEvent into a representative XML Document or XML String.
 *
 * This class is not synchronized.  Each visitor method modifies the same document.
 * The methods to directly convert a ScoreBoardEvent to a Document or String create
 * a new Document before their operation.
 */
public class ScoreBoardEventXmlVisitor extends DefaultScoreBoardEventVisitor implements ScoreBoardEventVisitor
{
	public ScoreBoardEventXmlVisitor() {
		super();
		setWarning(true);
		resetDocument();
	}

	public String toString(ScoreBoardEvent event) {
		try {
			return editor.toString(toDocument(event));
		} catch ( TransformerException tE ) {
			throw new RuntimeException("Should not happen: unexpected TransformerException: " + tE.getMessage());
		}
	}

	public String toString() {
		try {
			return editor.toString(toDocument());
		} catch ( TransformerException tE ) {
			throw new RuntimeException("Should not happen: unexpected TransformerException: " + tE.getMessage());
		}
	}

	public Document toDocument(ScoreBoardEvent event) {
		resetDocument();
		event.accept(this);
		return document;
	}

	public Document toDocument() {
		return document;
	}

	public void resetDocument() {
		document = editor.createDocument("ScoreBoard");
	}

	public void scoreBoardAddScoreBoardImage(ScoreBoard scoreBoard, ScoreBoardImage scoreBoardImage) {
		Element e = converter.toElement(getScoreBoardElement(), scoreBoardImage);
		e.setAttribute("add", "true");
	}
	public void scoreBoardRemoveScoreBoardImage(ScoreBoard scoreBoard, ScoreBoardImage scoreBoardImage) {
		Element e = converter.toElement(getScoreBoardElement(), scoreBoardImage);
		e.setAttribute("remove", "true");
	}

	public void scoreBoardTimeoutOwnerChange(ScoreBoard scoreBoard, String owner) {
		editor.setElement(getScoreBoardElement(), "TimeoutOwner", null, owner);
	}

	public void scoreBoardImageNameChange(ScoreBoardImage scoreBoardImage, String name) {
		editor.setElement(getScoreBoardImageElement(scoreBoardImage), "Name", null, name);
	}

	public void policyNameChange(Policy policy, String name) {
		editor.setElement(getPolicyElement(policy), "Name", null, name);
	}

	public void policyDescriptionChange(Policy policy, String description) {
		editor.setElement(getPolicyElement(policy), "Description", null, description);
	}

	public void policyEnabledChange(Policy policy, boolean enabled) {
		editor.setElement(getPolicyElement(policy), "Enabled", null, String.valueOf(enabled));
	}

	public void policyParameterValueChange(Policy.Parameter parameter, String value) {
		editor.setElement(getPolicyParameterElement(parameter), "Value", null, value);
	}

	public void clockNameChange(Clock clock, String name) {
		editor.setElement(getClockElement(clock), "Name", null, name);
	}

	public void clockNumberChange(Clock clock, int number) {
		editor.setElement(getClockElement(clock), "Number", null, String.valueOf(number));
	}
	public void clockMinimumNumberChange(Clock clock, int min) {
		editor.setElement(getClockElement(clock), "MinimumNumber", null, String.valueOf(min));
	}
	public void clockMaximumNumberChange(Clock clock, int max) {
		editor.setElement(getClockElement(clock), "MaximumNumber", null, String.valueOf(max));
	}

	public void clockTimeChange(Clock clock, long time) {
		editor.setElement(getClockElement(clock), "Time", null, String.valueOf(time));
	}
	public void clockMinimumTimeChange(Clock clock, long min) {
		editor.setElement(getClockElement(clock), "MinimumTime", null, String.valueOf(min));
	}
	public void clockMaximumTimeChange(Clock clock, long max) {
		editor.setElement(getClockElement(clock), "MaximumTime", null, String.valueOf(max));
	}

	public void clockDirectionChange(Clock clock, boolean down) {
		editor.setElement(getClockElement(clock), "Direction", null, String.valueOf(down));
	}

	public void clockRunningChange(Clock clock, boolean running) {
		editor.setElement(getClockElement(clock), "Running", null, String.valueOf(running));
	}

	public void teamNameChange(Team team, String name) {
		editor.setElement(getTeamElement(team), "Name", null, name);
	}

	public void teamScoreChange(Team team, int score) {
		editor.setElement(getTeamElement(team), "Score", null, String.valueOf(score));
	}

	public void teamTimeoutsChange(Team team, int timeouts) {
		editor.setElement(getTeamElement(team), "Timeouts", null, String.valueOf(timeouts));
	}

	public void teamAddSkater(Team team, Skater skater) {
		Element e = converter.toElement(getTeamElement(team), skater);
		e.setAttribute("add", "true");
	}

	public void teamRemoveSkater(Team team, Skater skater) {
		Element e = converter.toElement(getTeamElement(team), skater);
		e.setAttribute("remove", "true");
	}

	public void teamPositionChange(Team team, String position, Skater skater) {
		editor.setElement(getTeamElement(team), "Position", position, (null==skater?"":skater.getName()));
	}

	public void teamLeadJammerChange(Team team, boolean leadJammer) {
		editor.setElement(getTeamElement(team), "LeadJammer", null, String.valueOf(leadJammer));
	}

	public void teamLogoIdChange(TeamLogo teamLogo, String id) {
		editor.setElement(getTeamLogoElement(teamLogo), "Id", null, id);
	}

	public void teamLogoTypeChange(TeamLogo teamLogo, String type) {
		editor.setElement(getTeamLogoElement(teamLogo), "Type", null, type);
	}

	public void teamLogoNameChange(TeamLogo teamLogo, String name) {
		editor.setElement(getTeamLogoElement(teamLogo), "Name", null, name);
	}

	public void teamLogoDirectoryChange(TeamLogo teamLogo, String directory) {
		editor.setElement(getTeamLogoElement(teamLogo), "Directory", null, directory);
	}
	public void teamLogoFilenameChange(TeamLogo teamLogo, String filename) {
		editor.setElement(getTeamLogoElement(teamLogo), "Filename", null, filename);
	}

	public void skaterNameChange(Skater skater, String name) {
		editor.setElement(getSkaterElement(skater), "Name", null, name);
	}

	public void skaterNumberChange(Skater skater, String number) {
		editor.setElement(getSkaterElement(skater), "Number", null, String.valueOf(number));
	}

	public void skaterPositionChange(Skater skater, String position) {
		editor.setElement(getSkaterElement(skater), "Position", null, position);
	}

	public void skaterLeadJammerChange(Skater skater, boolean leadJammer) {
		editor.setElement(getSkaterElement(skater), "LeadJammer", null, String.valueOf(leadJammer));
	}

	public void skaterPenaltyBoxChange(Skater skater, boolean penaltyBox) {
		editor.setElement(getSkaterElement(skater), "PenaltyBox", null, String.valueOf(penaltyBox));
	}

	public void skaterCurrentPassChange(Skater skater, int currentPass) {
		editor.setElement(getSkaterElement(skater), "CurrentPass", null, String.valueOf(currentPass));
	}

	public void passScoreChange(Pass pass, int score) {
		editor.setElement(getPassElement(pass), "Score", null, String.valueOf(score));
	}



	protected Element getScoreBoardElement() {
		return editor.getElement(document.getDocumentElement(), "ScoreBoard");
	}

	protected Element getScoreBoardImageElement(ScoreBoardImage scoreBoardImage) {
		return editor.getElement(getScoreBoardElement(), "Image", scoreBoardImage.getId());
	}

	protected Element getPolicyElement(Policy policy) {
		return editor.getElement(getScoreBoardElement(), "Policy", policy.getId());
	}

	protected Element getPolicyParameterElement(Policy.Parameter parameter) {
		return editor.getElement(getPolicyElement(parameter.getPolicy()), "Parameter", parameter.getName());
	}

	protected Element getClockElement(Clock clock) {
		return editor.getElement(getScoreBoardElement(), "Clock", clock.getId());
	}

	protected Element getTeamElement(Team team) {
		return editor.getElement(getScoreBoardElement(), "Team", team.getId());
	}

	protected Element getTeamLogoElement(TeamLogo teamLogo) {
		return editor.getElement(getTeamElement(teamLogo.getTeam()), "TeamLogo");
	}

	protected Element getSkaterElement(Skater skater) {
		return editor.getElement(getTeamElement(skater.getTeam()), "Skater", skater.getId());
	}

	protected Element getPassElement(Pass pass) {
		return editor.getElement(getSkaterElement(pass.getSkater()), "Pass", pass.getId());
	}

	protected Document document;

	private XmlDocumentEditor editor = new XmlDocumentEditor();
	private ScoreBoardXmlConverter converter = new ScoreBoardXmlConverter();
}
