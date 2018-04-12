
  Feature: Test a Crime case in megistrate court

    Scenario: an employed citizen with partner and children (partial means assesment)

      Given a INDICTABLE and MAGISTRATE case:

      And citizen is employed with following income:
      															| pay |  incomeTax | nationalInsurance | frequency |
      															| 720 | 0 | 0 | monthly |

      And citizen receives the following child tax benefit:
                                                    | incomeType | amount | frequency |
                                                    | CHILD_TAX_CREDIT | 120 | WEEKLY |
                                                    
                                                    
       And citizen receives the following miscellanious benefit:
                                                    | incomeType | amount | frequency |
                                                    | MISCELLANEOUS | 672 | MONTHLY | 


      And citizen has following partner with other income:
                                                      | incomeType| amount | frequency|
                                                      | CHILD_BENEFIT  | 33 | WEEKLY |
                                                                                               
    And citizen has following children:
    												    | name | relationToApplicant | lowerAgeRange | upperAgeRange | numberOfChildren |
    													| jon  | child_of_applicant | 8 | 10 | 1 |
    													| david  | child_of_applicant | 13 | 15 | 1 |
    															
    
      When  rule engine is executed
      Then citizen is "EMPLOYED"
      Then citizen employed income is 8640.00
      Then citizen adjusted income is 10024.39
      Then citizen gross combined household income is 24660.00
      Then total weighting  is 2.46
      Then adjustedIncomeBelowLowerThreshold is true
      Then court type is MAGISTRATE
      Then case type is INDICTABLE
      Then citizen PASSED means test
      
      
      
      
 Scenario: an employed citizen with children and no partner(full means assesment) 

    Given a SUMMARY_ONLY and MAGISTRATE case:

    And citizen is employed with following income:
      															| pay |  incomeTax | nationalInsurance | frequency |
      															| 2500 | 310 | 250 | monthly |

    And citizen receives the following child tax benefit:
                                                    | incomeType | amount | frequency |
                                                    | CHILD_BENEFIT | 33 | WEEKLY |


    And citizen receives the following maintenance benefit:
                                                    | incomeType | amount | frequency |
                                                    | MAINTENANCE_INCOME | 500 | MONTHLY |

    And citizen has following children:
    												  				| name | relationToApplicant | lowerAgeRange | upperAgeRange | numberOfChildren |
    																	| jon  | child_of_applicant | 2 | 4 | 1 |
    																	| david  | child_of_applicant | 11 | 12 | 1 |

    And citizen has following outgoings:
    															| outGoingType | amount | frequency |
    															| CHILD_CARE_FEES | 300 | monthly|
    															| MORTGAGE_PAYMENT	| 600 | monthly |
    															| COUNCIL_TAX	| 1580 | ANNUAL |
      When  rule engine is executed
      Then citizen is "EMPLOYED"
      Then citizen employed income is 30000.00
      Then citizen adjusted income is 22056.14
      Then citizen gross combined household income is 37716.00
      Then citizen disposable annual income is 8910.04
      #Then citizen annual outgoings is 19100.0
      Then total weighting  is 1.71
      Then adjustedIncomeBelowLowerThreshold is false
      Then court type is MAGISTRATE
      Then case type is SUMMARY_ONLY
      Then citizen FAILED means test
      
      
      
      
    Scenario: an employed citizen, no dependent , no partner  (partial means assesment)

      Given a EITHER_WAY and MAGISTRATE case:

      And citizen is employed with following income:
      															| pay |  incomeTax | nationalInsurance | frequency |
      															| 4000 | 0 | 0 | monthly |

      When  rule engine is executed
      Then citizen is "EMPLOYED"
      #Then citizen employed income is 8640.00
      #Then citizen adjusted income is 10024.39
      #Then citizen gross combined household income is 24660.00
      Then total weighting  is 1.00
      Then adjustedIncomeBelowLowerThreshold is false
      Then court type is MAGISTRATE
      Then case type is EITHER_WAY
      Then citizen FAILED means test   
      