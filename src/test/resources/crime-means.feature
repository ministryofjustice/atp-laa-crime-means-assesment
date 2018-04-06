
  Feature: Test Crime case with an employed applicant

    Scenario: Citizen with a meg court case having a partner and two children

      Given a meg court case

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
    												    | name | relationToApplicant | childAge |
    													| jon  | child_of_applicant | 8 |
    													| david  | child_of_applicant | 14 |
    															
    
      When  rule engine is executed
      Then citizen employed income is 8640
      Then citizen adjusted income is 10024.39
      Then citizen gross combined household income is 24660
      Then total weighting  is 2.46
      Then adjustedIncomeBelowLowerThreshold is true
      Then citizen passed means test is PASSED
      
      
      








  

    
