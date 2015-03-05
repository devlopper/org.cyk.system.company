package org.cyk.system.company.ui.web.primefaces.page;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.time.DateUtils;
import org.cyk.system.company.business.api.product.SaleBusiness;
import org.cyk.system.company.model.product.SaleSearchCriteria;
import org.cyk.system.root.business.api.time.TimeDivisionTypeBusiness;
import org.cyk.system.root.model.time.TimeDivisionType;
import org.cyk.ui.web.primefaces.page.AbstractChartPage;
import org.joda.time.DateTimeConstants;
import org.joda.time.MutableDateTime;
import org.primefaces.model.chart.BarChartModel;

@Named @ViewScoped @Getter @Setter
public class SaleStatisticsPeriodPage extends AbstractChartPage implements Serializable {

	private static final long serialVersionUID = 9040359120893077422L;

	@Inject private SaleBusiness saleBusiness;
	@Inject private TimeDivisionTypeBusiness timeDivisionTypeBusiness;
 	
	private TimeDivisionType selectedTimeDivisionType;
	private List<TimeDivisionType> timeDivisionTypes; 
	private SaleSearchCriteria saleSearchCriteria;
	
 	private BarChartModel turnoverBarChartModel,countBarChartModel;
    
	@Override
	protected void initialisation() {
		super.initialisation();
		saleSearchCriteria = new SaleSearchCriteria(DateUtils.addDays(new Date(),-4), DateUtils.addDays(new Date(),4));
		selectedTimeDivisionType = timeDivisionTypeBusiness.find(TimeDivisionType.DAY);
        update();
	}
	
	public void update(){
		MutableDateTime dt = new MutableDateTime(saleSearchCriteria.getToDateSearchCriteria().getValue());
		dt.setMillisOfDay(DateTimeConstants.MILLIS_PER_DAY-1);
		
		saleSearchCriteria.getToDateSearchCriteria().setValue(dt.toDate());
		turnoverBarChartModel = chartManager.barModel(saleBusiness.findTurnOverStatistics(saleSearchCriteria, selectedTimeDivisionType));
        countBarChartModel = chartManager.barModel(saleBusiness.findCountStatistics(saleSearchCriteria, selectedTimeDivisionType));
	}
	 
}