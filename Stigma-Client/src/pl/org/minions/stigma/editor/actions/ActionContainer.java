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
package pl.org.minions.stigma.editor.actions;

import javax.swing.Action;

import pl.org.minions.stigma.editor.actions.category.ImportCategoryElementAction;
import pl.org.minions.stigma.editor.actions.category.NewCategoryElementAction;
import pl.org.minions.stigma.editor.actions.document.CloneDocumentAction;
import pl.org.minions.stigma.editor.actions.document.DeleteDocumentAction;
import pl.org.minions.stigma.editor.actions.document.ExportDocumentAction;
import pl.org.minions.stigma.editor.actions.document.OpenDocumentAction;
import pl.org.minions.stigma.editor.actions.help.AboutEditorAction;
import pl.org.minions.stigma.editor.actions.resourceset.LoadResourceSetAction;
import pl.org.minions.stigma.editor.actions.resourceset.NewResourceSetAction;
import pl.org.minions.stigma.editor.actions.resourceset.SaveResourceSetAction;

/**
 * Class that holds all of Action classes.
 */
public final class ActionContainer
{
    public static final Action NEW_RESOURCE_SET = new NewResourceSetAction();
    public static final Action LOAD_RESOURCE_SET = new LoadResourceSetAction();
    public static final Action SAVE_RESOURCE_SET = new SaveResourceSetAction();

    public static final Action CLOSE_EDITOR = new CloseEditorAction();

    public static final Action NEW_CATEGORY_ELEMENT =
            new NewCategoryElementAction();
    public static final Action IMPORT_CATEGORY_ELEMENT =
            new ImportCategoryElementAction();

    public static final Action OPEN_DOCUMENT = new OpenDocumentAction();
    public static final Action DELETE_DOCUMENT = new DeleteDocumentAction();
    public static final Action CLONE_DOCUMENT = new CloneDocumentAction();
    public static final Action EXPORT_DOCUMENT = new ExportDocumentAction();

    public static final Action ABOUT_EDITOR = new AboutEditorAction();

    private ActionContainer()
    {

    }
}
