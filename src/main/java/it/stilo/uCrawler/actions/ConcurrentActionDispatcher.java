package it.stilo.uCrawler.actions;

/*
 * #%L
 * uCrawler
 * %%
 * Copyright (C) 2012 - 2018 Giovanni Stilo
 * %%
 * uCrawler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/lgpl-3.0.txt>.
 * #L%
 */

import it.stilo.uCrawler.core.actions.ActionIF;
import it.stilo.uCrawler.core.actions.ActionsException;
import it.stilo.uCrawler.page.Page;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stilo
 */
public class ConcurrentActionDispatcher implements ActionIF {

    private LinkedBlockingQueue<Page> pagesBuffer = new LinkedBlockingQueue<Page>();
    //private long defaultWait = 10000;

    public ConcurrentActionDispatcher(ActionIF action, int numberOfThreads) {
        for (int i = 0; i < numberOfThreads; i++) {
            new ActionPerformer(pagesBuffer, action).start();
        }
    }

    @Override
    public boolean doSomething(Page page) throws ActionsException {
        try {
            this.pagesBuffer.put(page);
        } catch (InterruptedException ex) {
            throw new ActionsException(ex);
        }

        return true;
    }

    private class ActionPerformer extends Thread {

        private LinkedBlockingQueue<Page> pagesBuffer;
        private ActionIF action;

        public ActionPerformer(LinkedBlockingQueue<Page> pagesBuffer, ActionIF action) {
            this.action = action;
            this.pagesBuffer = pagesBuffer;
        }

        @Override
        public void run() {
            for (;;) {
                Page p;
                try {
                    p = pagesBuffer.take();
                    action.doSomething(p);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConcurrentActionDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ActionsException ex) {
                    Logger.getLogger(ConcurrentActionDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
}

