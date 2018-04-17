package com.uk.moj.laa.atp.crime.steps;

import com.laa.model.*;
import com.laa.model.civil.enums.EmploymentStatus;
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

    @Given("^a ([^\"]*) and ([^\"]*) case:")
    public void caseWithEmployed(CaseType caseType, CourtType courtType) {

        this.crimeCase = new CrimeCase();
        crimeCase.setCaseType(caseType);
        crimeCase.setCourtType(courtType);
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
    
    @And("^citizen receives the following maintenance benefit:")
    public void citizenMaintenanceIcome(List<OtherIncome> otherIncomes) {

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
    
    @And("^citizen has following partner with EmployedIncome income:")
    public void partnerIncome(List<EmployedIncome> employedIncome) {

        Partner partner = ofNullable(means.getPartner()).orElse(new Partner());
        partner.setEmployedIncome(employedIncome.get(0));
        means.setPartner(partner);
        crimeCase.setMeansInformation(means);
    }

    
    @And("^citizen has following children:")
    public void citizenChildren(List<CrimeDependent> crimeDependents) {

    	means.setDependents(ofNullable(means.getDependents()).orElse(new ArrayList<>()));
        means.getDependents().addAll(crimeDependents);
        crimeCase.setMeansInformation(means);
    }

    @And("^citizen has following outgoings:")
    public void citizenHasOutgoings(List<Outgoing> outgoings) throws Throwable {

        means.setOutgoings(outgoings);
    }

    @When("^rule engine is executed$")
    public void executeDecisionRules() {

        this.decisionReport = new CrimeMeansDecisionReport();
        kieSession.execute(Stream.of(crimeCase, decisionReport).collect(Collectors.toList()));
    }


    @Then("^citizen is \"([^\"]*)\"$")
    public void applicantIsEmployed(EmploymentStatus status) {
        assertThat(decisionReport.getEmploymentStatus()).isEqualTo(status);
    }

    @Then("^citizen employed income is (\\d+.\\d+)$")
    public void applicantEmployedIncomeIs(BigDecimal expectedEmployedIncome) {
        assertThat(decisionReport.getEmploymentIncome()).isEqualTo(expectedEmployedIncome.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @Then("^citizen gross combined household income is (\\d+.\\d+)$")
    public void applicantGrossHouseHoldIncomeIs(BigDecimal expectedGrossHouseHoldIncome) {
        assertThat(decisionReport.getGrossHouseholdIncome()).isEqualTo(expectedGrossHouseHoldIncome.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @Then("^citizen adjusted income is (\\d+.\\d+)$")
    public void applicantAdjustedIncomeIs(BigDecimal expectedAdjustedIncome) {
        assertThat(decisionReport.getAdjustedIncome()).isEqualTo(expectedAdjustedIncome.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @Then("^citizen disposable annual income is (\\d+.\\d+)$")
    public void applicantDisposableIncomeIs(BigDecimal disposableIncome) {
        assertThat(decisionReport.getDisposableIncome()).isEqualTo(disposableIncome.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @Then("^citizen annual outgoings is (\\d+.\\d+)$")
    public void applicantOutgoingIs(BigDecimal annualOutgoings) {
        assertThat(decisionReport.getTotalOutgoings()).isEqualTo(annualOutgoings);
    }

    @Then("^total weighting  is (\\d+.\\d+)$")
    public void totalWeightingIs(BigDecimal weighting) throws Throwable {
        assertThat(decisionReport.getTotalWeight()).isEqualTo(weighting);
    }

    @Then("^adjustedIncomeBelowLowerThreshold is ([^\"]*)$")
    public void adjustedIncomeBelowLowerThreshold(boolean adjustedIncomeBelowLowerThreshold) {
        assertThat(decisionReport.isAdjustedIncomeBelowLowerThreshold()).isEqualTo(adjustedIncomeBelowLowerThreshold);
    }
    
    @Then("^court type is ([^\"]*)$")
    public void courtType(CourtType courtType) {
        assertThat(decisionReport.getCourtType()).isEqualTo(courtType);
    }
    
    @Then("^case type is ([^\"]*)$")
    public void caseType(CaseType caseType) {
        assertThat(decisionReport.getCaseType()).isEqualTo(caseType);
    }

    @Then("^citizen ([^\"]*) means test$")
    public void meansPassed(CrimeAssessmentResult result) {
        assertThat(decisionReport.getCrimeAssessmentResult()).isEqualTo(result);
    }

}
