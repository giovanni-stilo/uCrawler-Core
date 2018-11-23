package it.stilo.uCrawler.concurrentFetcher.configuration;

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

import java.util.Random;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author stilo
 */
public class RandomValueConstraint extends NumericConstraintIF {

    private double min;
    private double max;
    private Random generator;

    public RandomValueConstraint() {

        generator = new Random(System.currentTimeMillis());
    }

    public double getMin() {
        return min;
    }

    @Required
    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    @Required
    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public double next() {
        return (generator.nextDouble() * (this.max - this.min)) + this.min;
    }

    @Override
    public NumericConstraintIF clone() {
        RandomValueConstraint newOne = new RandomValueConstraint();
        newOne.setMax(max);
        newOne.setMin(min);
        return newOne;
        
    }

   

   

}
