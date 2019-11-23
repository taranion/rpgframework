package de.rpgframework.print;

import de.rpgframework.character.RuleSpecificCharacterObject;

import java.util.List;

/**
 * @author Stefan
 *
 */
public interface PDFPrintElement {

    byte[] render(RenderingParameter parameter);

    String getId();

    int getRequiredColumns();

    boolean hasFeature(PDFPrintElementFeature feature);

    List<String> getIndexableObjectNames(RuleSpecificCharacterObject character);

    List<String> getFilterOptions();

    String getDescription();

    int getNextHorizontalGrowth(RenderingParameter parameter);

    int getPreviousHorizontalGrowth(RenderingParameter parameter);

    class RenderingParameter {

        private Orientation orientation;
        private int verticalGrowthOffset = 0;
        private int horizontalGrowthOffset = 0;
        private int index = 0;
        private int filterOption = 0;

        public RenderingParameter() {
            this.orientation = Orientation.STANDALONE;
        }

        public RenderingParameter(Orientation orientation) {
            this.orientation = orientation;
        }

        //--------------------------------------------------------------------
        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
        	StringBuffer buf = new StringBuffer();
        	buf.append("orient="+orientation);
        	buf.append(", hori="+horizontalGrowthOffset);
        	return buf.toString();
        }

        public Orientation getOrientation() {
            return orientation;
        }
        public void setOrientation(Orientation orientation) {
            this.orientation = orientation;
        }

        public int getVerticalGrowthOffset() {
            return verticalGrowthOffset;
        }

        public void setVerticalGrowthOffset(int verticalGrowthOffset) {
            this.verticalGrowthOffset = verticalGrowthOffset;
        }

        public int getHorizontalGrowthOffset() {
            return horizontalGrowthOffset;
        }

        public void setHorizontalGrowthOffset(int horizontalGrowthOffset) {
            this.horizontalGrowthOffset = horizontalGrowthOffset;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getFilterOption() {
            return filterOption;
        }

        public void setFilterOption(int filterOption) {
            this.filterOption = filterOption;
        }
    }

    enum Orientation{
        LEFT,
        RIGHT,
        STANDALONE,
    }
}
