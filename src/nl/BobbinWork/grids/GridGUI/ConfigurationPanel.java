/* PolarGridDefinitionPanel.java Copyright 2005-2007 by J. Falkink-Pol
 *
 * This file is part of BobbinWork.
 *
 * BobbinWork is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BobbinWork is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BobbinWork.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.BobbinWork.grids.GridGUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static nl.BobbinWork.bwlib.gui.Localizer.getString;
import nl.BobbinWork.grids.PolarGridModel.DensityChange;
import nl.BobbinWork.grids.PolarGridModel.PolarGridModel;
/**
 *
 * @author  J. Falkink-Pol
 */
@SuppressWarnings("serial")
public class ConfigurationPanel extends JPanel {
    
    private static double MAX_DIAMETER=500d;
    private static double MIN_DIAMETER=1d;
    private static double DIAMETER_STEP=0.1d;
    
    private static String DIR = "nl/BobbinWork/grids/GridGUI/tooltipImages/";
    private String[] distanceComboStrings ={ 
    		comboRow(DIR+"DistOptNull.gif"), 
    		comboRow(DIR+"DistOptNew.gif"), 
    		comboRow(DIR+"DistOptAvg.gif"), 
    		comboRow(DIR+"DistOptOld.gif")}; // ascending order

    private PolarGridModel pgm = null;
    
    /** Creates new form PolarGridDefinitionPanel */
    public ConfigurationPanel(PolarGridModel pgm) {
        
        initComponents();
        setModel(pgm);
    }
    
    private JSpinner[] spinners() {
    	// as a field it was initialised too late
    	return new JSpinner[] {
    			spiNumberOfRepeats, 
    			spiDotsPerRepeat, 
    			spiAngleOnFootside, 
    			spiInnerDiameter,
    			spiOuterDiameter,
    			spiDiameterDensityChange};
    }
    
    private String comboRow(String imageFile) {
        return  "<html><img src='" + getClass().getClassLoader().getResource(imageFile) + "'> &nbsp; </html>";
    }
    
    public void setModel(PolarGridModel pgm) {
        this.pgm = pgm;
        this.connectComponents();
    }
    
    private void connectComponents( ) {
        
        DensityChange dc = (DensityChange) pgm.getInnerCircle().getNext();
        
        // initial values from pgm to editable panel fields
        spiNumberOfRepeats      .getModel().setValue(new Integer(pgm.getNumberOfRepeats()));
        spiDotsPerRepeat        .getModel().setValue(new Integer(pgm.getDotsPerRepeat()));
        spiAngleOnFootside      .getModel().setValue(new Integer(pgm.getAngleOnFootside()));
        spiInnerDiameter        .getModel().setValue(new Double (pgm.getInnerCircle().getDiameter()));
        spiOuterDiameter        .getModel().setValue(new Double (pgm.getOuterCircle().getDiameter()));
        spiDiameterDensityChange.getModel().setValue(new Double (dc.getDiameter()));
        //TODO: cmbLegRatio     .setText( String.valueOf( dc.getLegRatio() ));
        cmbLegRatio             .setPossibleRatios( pgm.getDotsPerRepeat() );
        cbxAlternate            .setSelected      ( dc.getAlternate() );
        cmbDistanceOption       .setSelectedIndex ( dc.getDistanceOption() );
        
        addModelToLabelListeners();
        
        // from panel fields to pgm
        spiNumberOfRepeats.getModel().addChangeListener( (ChangeListener) EventHandler.create(ChangeListener.class,
                pgm, "numberOfRepeats", "source.value") );
        spiDotsPerRepeat  .getModel().addChangeListener( (ChangeListener) EventHandler.create(ChangeListener.class,
                pgm, "dotsPerRepeat", "source.value") );
        spiAngleOnFootside.getModel().addChangeListener( (ChangeListener) EventHandler.create(ChangeListener.class,
                pgm, "angleOnFootside", "source.value") );
        spiInnerDiameter  .getModel().addChangeListener( (ChangeListener) EventHandler.create(ChangeListener.class,
                pgm.getInnerCircle(), "diameter", "source.value") );
        spiOuterDiameter  .getModel().addChangeListener( (ChangeListener) EventHandler.create(ChangeListener.class,
                pgm.getOuterCircle(), "diameter", "source.value") );
        spiDiameterDensityChange.getModel().addChangeListener( (ChangeListener) EventHandler.create(ChangeListener.class,
                dc, "diameter", "source.value") );
        cbxAlternate            .addActionListener        ( (ActionListener) EventHandler.create(ActionListener.class,
                dc, "alternate", "source.selected") );
        cmbDistanceOption       .addActionListener        ( (ActionListener) EventHandler.create(ActionListener.class,
                dc, "distanceOption", "source.selectedIndex") );
        cmbLegRatio             .addActionListener        ( (ActionListener) EventHandler.create(ActionListener.class,
                dc.getLegRatio(), "ratio", "source.text") );
        
        setDependencies();
    }

    /** Adds listeners for semantic events to the objects on this panel. 
     * @param toBeRepainted the component to be repainted by the listeners 
     * */
    public void addRepaintingListeners( final Component toBeRepainted ) {

        final class RepaintingListener implements ActionListener, ChangeListener {

        	public void stateChanged(ChangeEvent e) {
    			toBeRepainted.repaint();
    	    }
    	    
        	public void actionPerformed(ActionEvent e) {
    			toBeRepainted.repaint();
    	    }
        }
        
        RepaintingListener repaintingListener = new RepaintingListener();
        
    	cbxAlternate      .addActionListener( repaintingListener );
    	cmbLegRatio       .addActionListener( repaintingListener );
    	cmbDistanceOption .addActionListener( repaintingListener );

    	for (JSpinner spinner : spinners() ) {
    		spinner.getModel().addChangeListener(repaintingListener);
    	}
    }
    
    /** Adds listeners for semantic events to the objects on this panel. */
	private void addModelToLabelListeners() {
		// keep the computed labels up to date (triggering this.fromModelToLabels)
        ModelToLabelListener listener = new ModelToLabelListener();
    	for (JSpinner spinner : spinners() ) {
    		spinner.addChangeListener(listener);
    	}
    	cmbLegRatio      .addActionListener(listener);
        cmbDistanceOption.addActionListener(listener);
	}
    
    private class ModelToLabelListener implements ActionListener, ChangeListener {

    	private String roundString(double value) {
    		int v = (int) (value * 100D);
    		return String.valueOf( ((double)v) / 100 );
    	}
    	
    	public void stateChanged(ChangeEvent e) {
	        fromModelToLabels();
	    }
	    
    	public void actionPerformed(ActionEvent e) {
	        fromModelToLabels();
	    }
	    
	    /** Keeps the computed labels up to date. */
	    // TODO: called 10 times when the visible button is pushed, 5 times when focus changes to another field
	    private void fromModelToLabels() {
	    	
	    	DensityChange dc = ((DensityChange) pgm.getInnerCircle().getNext());
	    	
	    	int numberOfRpeats = pgm.getNumberOfRepeats();
	    	double densityCircumference = dc.getCircumference();
	    	double innerCircumeference = pgm.getInnerCircle().getCircumference();
	    	double outerCircumference = pgm.getOuterCircle().getCircumference();
	    	double averageCircumference = (innerCircumeference + outerCircumference ) / 2;
	    	
	    	lblCircumInside.setText( roundString( innerCircumeference ));
	    	lblCircumOutside.setText( roundString( outerCircumference ));
	    	lblCircumDensityChange.setText( roundString( densityCircumference ));
	    	
	    	int numberOfDots = pgm.getDotsPerRepeat() * numberOfRpeats;
	    	lblDistanceInside.setText( roundString( innerCircumeference / numberOfDots) );
	    	lblDistanceInsideStraight.setText( roundString( averageCircumference / numberOfDots) );
	    	lblDistanceOldDensityStraight.setText( roundString( averageCircumference / numberOfDots) );
	    	lblDistanceOldDensity.setText( roundString( densityCircumference / numberOfDots));
	    	
	    	numberOfDots = dc.getDotsPerRepeat() * numberOfRpeats;
	    	lblDistanceOutside.setText( roundString( outerCircumference / numberOfDots));
	    	lblDistanceOutsideStraight.setText( roundString( averageCircumference / numberOfDots) );
	    	lblDistanceNewDensityStraight.setText( roundString( averageCircumference / numberOfDots) );
	    	lblDistanceNewDensity.setText( roundString( densityCircumference / numberOfDots));
	    	
	    	setDependencies();
	    }
    }
    
    private void setDependencies() {
        
        cmbLegRatio.setPossibleRatios(pgm.getDotsPerRepeat());

        DensityChange dc = ((DensityChange) pgm.getInnerCircle().getNext());
        
        javax.swing.SpinnerNumberModel smInnerDiameter = (javax.swing.SpinnerNumberModel) spiInnerDiameter.getModel();
        smInnerDiameter.setMaximum(new Double(dc.getDiameter()));
        
        javax.swing.SpinnerNumberModel smDiameterDensityChange = (javax.swing.SpinnerNumberModel) spiDiameterDensityChange.getModel();
        smDiameterDensityChange.setMaximum(new Double(pgm.getOuterCircle().getDiameter()));
        smDiameterDensityChange.setMinimum(new Double(pgm.getInnerCircle().getDiameter()));
        
        javax.swing.SpinnerNumberModel smOuterDiameter = (javax.swing.SpinnerNumberModel) spiOuterDiameter.getModel();
        smOuterDiameter.setMinimum(new Double(dc.getDiameter()));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblNumberOfRepeats = new javax.swing.JLabel();
        spiNumberOfRepeats = new javax.swing.JSpinner();
        lblDotsPerRepeat = new javax.swing.JLabel();
        spiDotsPerRepeat = new javax.swing.JSpinner();
        lblAngleOnFootside = new javax.swing.JLabel();
        spiAngleOnFootside = new javax.swing.JSpinner();
        lblDiameters = new javax.swing.JLabel();
        lblCircumference = new javax.swing.JLabel();
        lblDotDistances = new javax.swing.JLabel();
        lblDotDistanceStraight = new javax.swing.JLabel();
        lblDotDistanceCurved = new javax.swing.JLabel();
        lblInSide = new javax.swing.JLabel();
        lblOutSide = new javax.swing.JLabel();
        spiInnerDiameter = new javax.swing.JSpinner();
        lblCircumInside = new javax.swing.JLabel();
        lblDistanceInside = new javax.swing.JLabel();
        lblDistanceInsideStraight = new javax.swing.JLabel();
        spiOuterDiameter = new javax.swing.JSpinner();
        lblCircumOutside = new javax.swing.JLabel();
        lblDistanceOutside = new javax.swing.JLabel();
        lblDistanceOutsideStraight = new javax.swing.JLabel();
        cmbLegRatio = new RatioComboBox();
        spiDiameterDensityChange = new javax.swing.JSpinner();
        lblCircumDensityChange = new javax.swing.JLabel();
        lblDistanceOldDensity = new javax.swing.JLabel();
        lblDistanceOldDensityStraight = new javax.swing.JLabel();
        lblDistanceNewDensity = new javax.swing.JLabel();
        lblDistanceNewDensityStraight = new javax.swing.JLabel();
        cbxAlternate = new javax.swing.JCheckBox();
        cmbDistanceOption = new javax.swing.JComboBox();
        lblDensityChange = new javax.swing.JLabel();
        btnDraw = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        setToolTipText("");
        lblNumberOfRepeats.setDisplayedMnemonic(getString("number_of_repeats_Underline").charAt(0));
        lblNumberOfRepeats.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblNumberOfRepeats.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblNumberOfRepeats.setLabelFor(spiNumberOfRepeats);
        lblNumberOfRepeats.setText(getString("number_of_repeats"));
        lblNumberOfRepeats.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        add(lblNumberOfRepeats, gridBagConstraints);
        lblNumberOfRepeats.getAccessibleContext().setAccessibleName(getString("number_of_repeats"));

        spiNumberOfRepeats.setModel(new javax.swing.SpinnerNumberModel(5, PolarGridModel.MIN_REPEATS, PolarGridModel.MAX_REPEATS, 1));
        spiNumberOfRepeats.setMaximumSize(new java.awt.Dimension(27, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(spiNumberOfRepeats, gridBagConstraints);

        lblDotsPerRepeat.setDisplayedMnemonic(getString("dots_per_repeat_Underline").charAt(0));
        lblDotsPerRepeat.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblDotsPerRepeat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDotsPerRepeat.setLabelFor(spiDotsPerRepeat);
        lblDotsPerRepeat.setText(getString("dots_per_repeat"));
        lblDotsPerRepeat.setToolTipText(getString("dots_per_repeat_ToolTip"));
        lblDotsPerRepeat.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblDotsPerRepeat, gridBagConstraints);
        lblDotsPerRepeat.getAccessibleContext().setAccessibleName(getString("dots_per_repeat"));
        lblDotsPerRepeat.getAccessibleContext().setAccessibleDescription("");

        spiDotsPerRepeat.setModel(new javax.swing.SpinnerNumberModel(12, PolarGridModel.MIN_DOTS_PER_REPEAT, PolarGridModel.MAX_DOTS_PER_REPEAT, 1));
        spiDotsPerRepeat.setMaximumSize(new java.awt.Dimension(27, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(spiDotsPerRepeat, gridBagConstraints);

        lblAngleOnFootside.setDisplayedMnemonic(getString("angle_on_footside_Underline").charAt(0));
        lblAngleOnFootside.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblAngleOnFootside.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblAngleOnFootside.setLabelFor(spiAngleOnFootside);
        lblAngleOnFootside.setText(getString("angle_on_footside"));
        lblAngleOnFootside.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(lblAngleOnFootside, gridBagConstraints);
        lblAngleOnFootside.getAccessibleContext().setAccessibleName(getString("angle_on_footside"));

        spiAngleOnFootside.setModel(new javax.swing.SpinnerNumberModel(45, PolarGridModel.MIN_ANGLE, PolarGridModel.MAX_ANGLE, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        add(spiAngleOnFootside, gridBagConstraints);

        lblDiameters.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblDiameters.setText(getString("Matrix_Col_Head_diameters"));
        lblDiameters.setToolTipText(getString("Matrix_Col_Head_diameters_ToolTip"));
        lblDiameters.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDiameters, gridBagConstraints);
        lblDiameters.getAccessibleContext().setAccessibleName(getString("Matrix_Col_Head_diameters"));
        lblDiameters.getAccessibleContext().setAccessibleDescription(getString("Matrix_Col_Head_diameters_AccDesc"));

        lblCircumference.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblCircumference.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCircumference.setText(getString("Matrix_Col_Head_circumference"));
        lblCircumference.setToolTipText(getString("Matrix_Col_Head_circumference_ToolTip"));
        lblCircumference.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblCircumference, gridBagConstraints);
        lblCircumference.getAccessibleContext().setAccessibleName(getString("Matrix_Col_Head_circumference"));
        lblCircumference.getAccessibleContext().setAccessibleDescription(getString("Matrix_Col_Head_circumference_AccDesc"));

        lblDotDistances.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblDotDistances.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDotDistances.setText(getString("Matrix_Col_Head_distance"));
        lblDotDistances.setToolTipText(getString("Matrix_Col_Head_distance_ToolTip"));
        lblDotDistances.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDotDistances, gridBagConstraints);
        lblDotDistances.getAccessibleContext().setAccessibleName(getString("Matrix_Col_Head_distance"));
        lblDotDistances.getAccessibleContext().setAccessibleDescription(getString("Matrix_Col_Head_distances_AccDesc"));

        lblDotDistanceStraight.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblDotDistanceStraight.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDotDistanceStraight.setText(getString("Matrix_Col_Head_straight"));
        lblDotDistanceStraight.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDotDistanceStraight, gridBagConstraints);

        lblDotDistanceCurved.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblDotDistanceCurved.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDotDistanceCurved.setText(getString("Matrix_Col_Head_curved"));
        lblDotDistanceCurved.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDotDistanceCurved, gridBagConstraints);

        lblInSide.setDisplayedMnemonic(getString("Matrix_Row_Head_inside_Underline").charAt(0));
        lblInSide.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblInSide.setLabelFor(spiInnerDiameter);
        lblInSide.setText(getString("Matrix_Row_Head_inside"));
        lblInSide.setFocusable(false);
        lblInSide.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblInSide, gridBagConstraints);
        lblInSide.getAccessibleContext().setAccessibleName(getString("Matrix_Row_Head_inside"));
        lblInSide.getAccessibleContext().setAccessibleDescription(getString("Matrix_Row_Head_inside_AccDesc"));

        lblOutSide.setDisplayedMnemonic(getString("Matrix_Row_Head_outside_Underline").charAt(0));
        lblOutSide.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblOutSide.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblOutSide.setLabelFor(spiOuterDiameter);
        lblOutSide.setText(getString("Matrix_Row_Head_outside"));
        lblOutSide.setFocusable(false);
        lblOutSide.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblOutSide, gridBagConstraints);
        lblOutSide.getAccessibleContext().setAccessibleName(getString("Matrix_Row_Head_outside"));
        lblOutSide.getAccessibleContext().setAccessibleDescription(getString("Matrix_Row_Head_outside_AccDesc"));

        spiInnerDiameter.setModel(new javax.swing.SpinnerNumberModel(5d, MIN_DIAMETER, MAX_DIAMETER, DIAMETER_STEP));
        spiInnerDiameter.setMaximumSize(new java.awt.Dimension(27, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(spiInnerDiameter, gridBagConstraints);

        lblCircumInside.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblCircumInside.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCircumInside.setText(" . . ");
        lblCircumInside.setFocusable(false);
        lblCircumInside.setMaximumSize(new java.awt.Dimension(40, 22));
        lblCircumInside.setMinimumSize(new java.awt.Dimension(40, 22));
        lblCircumInside.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblCircumInside, gridBagConstraints);

        lblDistanceInside.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblDistanceInside.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDistanceInside.setText(" . . ");
        lblDistanceInside.setFocusable(false);
        lblDistanceInside.setMaximumSize(new java.awt.Dimension(40, 22));
        lblDistanceInside.setMinimumSize(new java.awt.Dimension(40, 22));
        lblDistanceInside.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDistanceInside, gridBagConstraints);

        lblDistanceInsideStraight.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblDistanceInsideStraight.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDistanceInsideStraight.setText(". .");
        lblDistanceInsideStraight.setMaximumSize(new java.awt.Dimension(40, 22));
        lblDistanceInsideStraight.setMinimumSize(new java.awt.Dimension(40, 22));
        lblDistanceInsideStraight.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDistanceInsideStraight, gridBagConstraints);

        spiOuterDiameter.setModel(new javax.swing.SpinnerNumberModel(5d, MIN_DIAMETER, MAX_DIAMETER, DIAMETER_STEP));
        spiOuterDiameter.setMaximumSize(new java.awt.Dimension(27, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(spiOuterDiameter, gridBagConstraints);

        lblCircumOutside.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblCircumOutside.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCircumOutside.setText(" . . ");
        lblCircumOutside.setFocusable(false);
        lblCircumOutside.setMaximumSize(new java.awt.Dimension(40, 22));
        lblCircumOutside.setMinimumSize(new java.awt.Dimension(40, 22));
        lblCircumOutside.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblCircumOutside, gridBagConstraints);

        lblDistanceOutside.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblDistanceOutside.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDistanceOutside.setText(" . . ");
        lblDistanceOutside.setFocusable(false);
        lblDistanceOutside.setMaximumSize(new java.awt.Dimension(40, 22));
        lblDistanceOutside.setMinimumSize(new java.awt.Dimension(40, 22));
        lblDistanceOutside.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDistanceOutside, gridBagConstraints);

        lblDistanceOutsideStraight.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblDistanceOutsideStraight.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDistanceOutsideStraight.setText(". .");
        lblDistanceOutsideStraight.setMaximumSize(new java.awt.Dimension(40, 22));
        lblDistanceOutsideStraight.setMinimumSize(new java.awt.Dimension(40, 22));
        lblDistanceOutsideStraight.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDistanceOutsideStraight, gridBagConstraints);

        cmbLegRatio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1:1", "1:2" }));
        cmbLegRatio.setToolTipText("");
        cmbLegRatio.setFont(new java.awt.Font("SansSerif", 0, 11));
        cmbLegRatio.setPreferredSize(new java.awt.Dimension(58, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(cmbLegRatio, gridBagConstraints);
        cmbLegRatio.getAccessibleContext().setAccessibleName(getString("Matrix_ratio"));

        spiDiameterDensityChange.setModel(new javax.swing.SpinnerNumberModel(5d, MIN_DIAMETER, MAX_DIAMETER, DIAMETER_STEP));
        spiDiameterDensityChange.setMaximumSize(new java.awt.Dimension(27, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(spiDiameterDensityChange, gridBagConstraints);

        lblCircumDensityChange.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblCircumDensityChange.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCircumDensityChange.setText(" . . ");
        lblCircumDensityChange.setFocusable(false);
        lblCircumDensityChange.setMaximumSize(new java.awt.Dimension(40, 22));
        lblCircumDensityChange.setMinimumSize(new java.awt.Dimension(40, 22));
        lblCircumDensityChange.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblCircumDensityChange, gridBagConstraints);

        lblDistanceOldDensity.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblDistanceOldDensity.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDistanceOldDensity.setText(" . . ");
        lblDistanceOldDensity.setToolTipText(getString("Matrix_old_density_ToolTip"));
        lblDistanceOldDensity.setFocusable(false);
        lblDistanceOldDensity.setMaximumSize(new java.awt.Dimension(40, 22));
        lblDistanceOldDensity.setMinimumSize(new java.awt.Dimension(40, 22));
        lblDistanceOldDensity.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDistanceOldDensity, gridBagConstraints);
        lblDistanceOldDensity.getAccessibleContext().setAccessibleName(getString("Matrix_old_density"));
        lblDistanceOldDensity.getAccessibleContext().setAccessibleDescription("");

        lblDistanceOldDensityStraight.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblDistanceOldDensityStraight.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDistanceOldDensityStraight.setText(". .");
        lblDistanceOldDensityStraight.setMaximumSize(new java.awt.Dimension(40, 22));
        lblDistanceOldDensityStraight.setMinimumSize(new java.awt.Dimension(40, 22));
        lblDistanceOldDensityStraight.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDistanceOldDensityStraight, gridBagConstraints);

        lblDistanceNewDensity.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblDistanceNewDensity.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDistanceNewDensity.setText(" . . ");
        lblDistanceNewDensity.setToolTipText(getString("Matrix_new_density_ToolTip"));
        lblDistanceNewDensity.setFocusable(false);
        lblDistanceNewDensity.setMaximumSize(new java.awt.Dimension(40, 22));
        lblDistanceNewDensity.setMinimumSize(new java.awt.Dimension(40, 22));
        lblDistanceNewDensity.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDistanceNewDensity, gridBagConstraints);
        lblDistanceNewDensity.getAccessibleContext().setAccessibleName(getString("Matrix_new_density"));
        lblDistanceNewDensity.getAccessibleContext().setAccessibleDescription("");

        lblDistanceNewDensityStraight.setFont(new java.awt.Font("SansSerif", 0, 11));
        lblDistanceNewDensityStraight.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDistanceNewDensityStraight.setText(". .");
        lblDistanceNewDensityStraight.setMaximumSize(new java.awt.Dimension(40, 22));
        lblDistanceNewDensityStraight.setMinimumSize(new java.awt.Dimension(40, 22));
        lblDistanceNewDensityStraight.setPreferredSize(new java.awt.Dimension(40, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDistanceNewDensityStraight, gridBagConstraints);

        cbxAlternate.setToolTipText("<html>"
            +"<img src='"
            +getClass()
            .getClassLoader()
            .getResource(DIR + "alternate.gif")
            +"'></html>");
        cbxAlternate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        cbxAlternate.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(cbxAlternate, gridBagConstraints);
        cbxAlternate.getAccessibleContext().setAccessibleName(getString("Matrix_alternate"));

        cmbDistanceOption.setFont(new java.awt.Font("SansSerif", 0, 11));
        cmbDistanceOption.setModel(new javax.swing.DefaultComboBoxModel(distanceComboStrings));
        cmbDistanceOption.setToolTipText(getString("Matrix_Density_Option_Tooltip"));
        cmbDistanceOption.setMinimumSize(new java.awt.Dimension(90, 32));
        cmbDistanceOption.setPreferredSize(new java.awt.Dimension(90, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        add(cmbDistanceOption, gridBagConstraints);

        lblDensityChange.setDisplayedMnemonic(getString("Matrix_Row_Head_density_change_Underline").charAt(0));
        lblDensityChange.setFont(new java.awt.Font("MS Sans Serif", 1, 12));
        lblDensityChange.setLabelFor(spiDiameterDensityChange);
        lblDensityChange.setText(getString("Matrix_Row_Head_density_change"));
        lblDensityChange.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblDensityChange, gridBagConstraints);
        lblDensityChange.getAccessibleContext().setAccessibleName(getString("Matrix_Row_Head_density_change"));
        lblDensityChange.getAccessibleContext().setAccessibleDescription(getString("Matrix_Row_Head_density_changes_AccDesc"));

        btnDraw.setMnemonic(getString("Draw_Underline").charAt(0));
        btnDraw.setText(getString("Draw"));
        btnDraw.setToolTipText("F5");
        btnDraw.setActionCommand("refresh");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        add(btnDraw, gridBagConstraints);
        btnDraw.getAccessibleContext().setAccessibleName(getString("Draw"));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(14, 1));
        jPanel1.setPreferredSize(new java.awt.Dimension(14, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 3);
        add(jPanel1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(14, 1));
        jPanel2.setPreferredSize(new java.awt.Dimension(14, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 3);
        add(jPanel2, gridBagConstraints);

    }
    // </editor-fold>//GEN-END:initComponents
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDraw;
    private javax.swing.JCheckBox cbxAlternate;
    private javax.swing.JComboBox cmbDistanceOption;
    private RatioComboBox cmbLegRatio;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAngleOnFootside;
    private javax.swing.JLabel lblCircumDensityChange;
    private javax.swing.JLabel lblCircumInside;
    private javax.swing.JLabel lblCircumOutside;
    private javax.swing.JLabel lblCircumference;
    private javax.swing.JLabel lblDensityChange;
    private javax.swing.JLabel lblDiameters;
    private javax.swing.JLabel lblDistanceInside;
    private javax.swing.JLabel lblDistanceInsideStraight;
    private javax.swing.JLabel lblDistanceNewDensity;
    private javax.swing.JLabel lblDistanceNewDensityStraight;
    private javax.swing.JLabel lblDistanceOldDensity;
    private javax.swing.JLabel lblDistanceOldDensityStraight;
    private javax.swing.JLabel lblDistanceOutside;
    private javax.swing.JLabel lblDistanceOutsideStraight;
    private javax.swing.JLabel lblDotDistanceCurved;
    private javax.swing.JLabel lblDotDistanceStraight;
    private javax.swing.JLabel lblDotDistances;
    private javax.swing.JLabel lblDotsPerRepeat;
    private javax.swing.JLabel lblInSide;
    private javax.swing.JLabel lblNumberOfRepeats;
    private javax.swing.JLabel lblOutSide;
    private javax.swing.JSpinner spiAngleOnFootside;
    private javax.swing.JSpinner spiDiameterDensityChange;
    private javax.swing.JSpinner spiDotsPerRepeat;
    private javax.swing.JSpinner spiInnerDiameter;
    private javax.swing.JSpinner spiNumberOfRepeats;
    private javax.swing.JSpinner spiOuterDiameter;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>

}
