
  Feature: Means assesment for crime case in crown court

    Scenario: an unemployed citizen

      Given a TRIAL_IN_CROWN_COURT and CROWN case:
    															
      When  rule engine is executed
      Then citizen is "UNEMPLOYED"
      #Then citizen employed income is null
      #Then citizen adjusted income is 0.0
      #Then citizen gross combined household income is 24660.00
      Then adjustedIncomeBelowLowerThreshold is true
      Then total weighting  is 1.0
      Then court type is CROWN
      Then case type is TRIAL_IN_CROWN_COURT
      Then citizen PASSED means test
      
    Scenario: An employed citizen with employed partner and child (full means assesment: case 04)

      Given a Indictable and CROWN case:
      
	    And citizen is employed with following income:
  															| pay |  incomeTax | nationalInsurance | frequency |
  															| 2500 | 600 | 500 | monthly |
  															
		  And citizen receives the following child tax benefit:
                                                  | incomeType | amount | frequency |
                                                  | CHILD_BENEFIT | 20 | WEEKLY |	
                                                  
	    And citizen has following children:
	    												    | name | relationToApplicant | lowerAgeRange | upperAgeRange | numberOfChildren |
	    														| david  | child_of_applicant | 16 | 18 | 1 |		
	    													
      And citizen has following outgoings:
															| outGoingType | amount | frequency |
															| MORTGAGE_PAYMENT	| 700 | monthly |
															| COUNCIL_TAX	| 2000 | ANNUAL |
															
  		And citizen has following partner with EmployedIncome income:
                                                      | pay| incomeTax | nationalInsurance| frequency|
                                                      | 2000 | 0 | 0 | monthly |																																			
	      When  rule engine is executed
      Then citizen is "EMPLOYED"
      Then citizen employed income is 30000.00
    	#Then citizen partner income is 24000
      Then citizen adjusted income is 24681.61
      Then citizen gross combined household income is 55040.00
      Then adjustedIncomeBelowLowerThreshold is false
      Then total weighting  is 2.23
      Then court type is CROWN
      Then case type is INDICTABLE
      Then citizen PASSED_WITH_CONTRIBUTION means test
      
      # why disposable income is not having 2 decimal points £18,783 
      