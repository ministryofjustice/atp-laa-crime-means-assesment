package com.uk.moj.laa.atp.crime.steps;

import com.laa.model.*;
import com.laa.model.crime.*;
import com.uk.moj.laa.atp.crime.SpringIntegrationTest;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class CrimeMeansStepDefinitions extends SpringIntegrationTest {

    private CrimeCase crimeCase;
    private MeansInformation means;
    private Applicant applicant;
    private CrimeMeansDecisionReport decisionReport;

    @Autowired
    @Qualifier("crimeDecision")
    private StatelessKieSession kieSession;

    @Given("^a megistrate court case$")
    public void caseWithEmployed() {

        this.crimeCase = new CrimeCase();
        crimeCase.setCaseType(CaseType.INDICTABLE);
        crimeCase.setCourtType(CourtType.MAGISTRATE);
        means = ofNullable(crimeCase.getMeansInformation()).orElse(new MeansInformation());
        applicant = ofNullable(means.getApplicant()).orElse(new Applicant());
        means.setApplicant(applicant);
        crimeCase.setMeansInformation(means);
    }

    @And("^citizen is employed with following income:")
    public void citizenEmploymentIncome(List<EmployedIncome> employedIncomes) {

        Applicant applicant = ofNullable(means.getApplicant()).orElse(new Applicant());
        means.setApplicant(applicant);
        applicant.setEmployedIncomeHistory(employedIncomes);
        crimeCase.setMeansInformation(means);

    }

    @And("^citizen receives the following child tax benefit:")
    public void citizenOtherIncome(List<OtherIncome> otherIncomes) {

        Applicant applicant = ofNullable(means.getApplicant()).orElse(new Applicant());
        List<OtherIncome> exitingOtherIncome = ofNullable(applicant.getOtherIncome()).orElse(new ArrayList<OtherIncome>());
        exitingOtherIncome.addAll(otherIncomes);
        applicant.setOtherIncome(exitingOtherIncome);
        means.setApplicant(applicant);
    }

    @And("^citizen receives the following miscellanious benefit:")
    public void citizenMiscellaniousIcome(List<OtherIncome> otherIncomes) {

        List<OtherIncome> exitingOtherIncome = ofNullable(applicant.getOtherIncome()).orElse(new ArrayList<OtherIncome>());
        exitingOtherIncome.addAll(otherIncomes);
        applicant.setOtherIncome(exitingOtherIncome);
        means.setApplicant(applicant);
    }

    @And("^citizen has following partner with other income:")
    public void partnerOtherIncome(List<OtherIncome> otherIncome) {

        Partner partner = ofNullable(means.getPartner()).orElse(new Partner());
        partner.setOtherIncome(otherIncome);
        means.setPartner(partner);
        crimeCase.setMeansInformation(means);
    }

    @And("^citizen has following children:")
    public void citizenChildren(List<CrimeDependent> crimeDependents) {

    	means.setDependents(ofNullable(means.getDependents()).orElse(new ArrayList<>()));
        means.getDependents().addAll(crimeDependents);
        crimeCase.setMeansInformation(means);
    }

    @When("^rule engine is executed$")
    public void executeDecisionRules() {

        this.decisionReport = new CrimeMeansDecisionReport();
        kieSession.execute(Stream.of(crimeCase, decisionReport).collect(Collectors.toList()));
    }

    @Then("^citizen employed income is (\\d+.\\d+)$")
    public void applicantEmployedIncomeIs(BigDecimal expectedEmployedIncome) {
        assertThat(decisionReport.getEmploymentIncome()).isEqualTo(expectedEmployedIncome);
    }

    @Then("^citizen gross combined household income is (\\d+.\\d+)$")
    public void applicantGrossHouseHoldIncomeIs(BigDecimal expectedGrossHouseHoldIncome) {
        assertThat(decisionReport.getGrossHouseholdIncome()).isEqualTo(expectedGrossHouseHoldIncome.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @Then("^citizen adjusted income is (\\d+.\\d+)$")
    public void applicantAdjustedIncomeIs(BigDecimal expectedAdjustedIncome) {
        assertThat(decisionReport.getAdjustedIncome()).isEqualTo(expectedAdjustedIncome.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @Then("^total weighting  is (\\d+.\\d+)$")
    public void totalWeightingIs(BigDecimal weighting) throws Throwable {
        assertThat(weighting).isEqualTo(decisionReport.getTotalWeight());
    }

    @Then("^adjustedIncomeBelowLowerThreshold is true$")
    public void adjustedIncomeBelowLowerThreshold() {
        assertThat(true).isEqualTo(decisionReport.isAdjustedIncomeBelowLowerThreshold());
    }
    
    @Then("^court type is megistrate$")
    public void courtType() {
        assertThat(CourtType.MAGISTRATE).isEqualTo(decisionReport.getCourtType());
    }
    
    @Then("^case type is indictable$")
    public void caseType() {
        assertThat(CaseType.INDICTABLE).isEqualTo(decisionReport.getCaseType());
    }

    @Then("^citizen passed means test$")
    public void meansPassed() {
        assertThat(CrimeAssessmentResult.PASSED).isEqualTo(decisionReport.getCrimeAssessmentResult());
    }

}
