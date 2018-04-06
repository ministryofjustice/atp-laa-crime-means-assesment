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

    MeansInformation means;

    private CrimeMeansDecisionReport decisionReport;

    @Autowired
    @Qualifier("crimeDecision")
    private StatelessKieSession kieSession;

    @Given("^a meg court case$")
    public void caseWithEmployed() {

        this.crimeCase = new CrimeCase();
        crimeCase.setCaseType(CaseType.INDICTABLE);
        crimeCase.setCourtType(CourtType.MAGISTRATE);
        means = ofNullable(crimeCase.getMeansInformation()).orElse(new MeansInformation());
        crimeCase.setMeansInformation(means);
    }

    @And("^citizen is employed with following income:")
    public void citizenEmploymentIncome(List<EmployedIncome> employedIncomes) {

        Applicant applicant = ofNullable(means.getApplicant()).orElse(new Applicant());
        means.setApplicant(applicant);
        applicant.setEmployedIncomeHistory(employedIncomes);
        crimeCase.setMeansInformation(means);

    }

    @And("^citizen receives the following child tax benefit")
    public void citizenOtherIncome(List<OtherIncome> otherIncomes) {

        MeansInformation means = ofNullable(crimeCase.getMeansInformation()).orElse(new MeansInformation());
        Applicant applicant = ofNullable(means.getApplicant()).orElse(new Applicant());
        List<OtherIncome> exitingOtherIncome = ofNullable(applicant.getOtherIncome()).orElse(new ArrayList<OtherIncome>());
        exitingOtherIncome.addAll(otherIncomes);
        applicant.setOtherIncome(exitingOtherIncome);
        means.setApplicant(applicant);
    }

    @And("^citizen receives the following miscellanious benefit:")
    public void citizenMiscellaniousIcome(List<OtherIncome> otherIncomes) {

        MeansInformation means = ofNullable(crimeCase.getMeansInformation()).orElse(new MeansInformation());
        Applicant applicant = ofNullable(means.getApplicant()).orElse(new Applicant());
        List<OtherIncome> exitingOtherIncome = ofNullable(applicant.getOtherIncome()).orElse(new ArrayList<OtherIncome>());
        exitingOtherIncome.addAll(otherIncomes);
        applicant.setOtherIncome(exitingOtherIncome);
        means.setApplicant(applicant);
    }


    @And("^citizen has following partner with other income:")
    public void partnerOtherIncome(List<OtherIncome> otherIncome) {

        MeansInformation means = ofNullable(crimeCase.getMeansInformation()).orElse(new MeansInformation());
        Partner partner = ofNullable(means.getPartner()).orElse(new Partner());
        partner.setOtherIncome(otherIncome);
        means.setPartner(partner);
        crimeCase.setMeansInformation(means);

    }

    @And("^citizen has following children:")
    public void citizenChildren(List<CrimeDependent> dependents) {

        MeansInformation means = ofNullable(crimeCase.getMeansInformation()).orElse(new MeansInformation());
        if (means.getDependents() == null) {
            List<Dependent> crimeDependents = new ArrayList<>();
            means.setDependents(crimeDependents);
        }

        means.getDependents().addAll(dependents);
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

    @Then("^adjustedIncomeBelowLowerThreshold is (\\d+.\\d+)$")
    public void adjustedIncomeBelowLowerThreshold(boolean adjustedIncomeBelowLowerThreshold) {
        assertThat(adjustedIncomeBelowLowerThreshold).isEqualTo(decisionReport.isAdjustedIncomeBelowLowerThreshold());
    }

    @Then("^citizen passed means test is (\\d+.\\d+)$")
    public void meansPassed(String passed) {
        System.out.println("decisionReport.getCrimeAssessmentResult().toString()" + decisionReport.getCrimeAssessmentResult().toString());
        System.out.println("PASSED  is" + passed);
        assertThat(passed).isEqualTo(decisionReport.getCrimeAssessmentResult().toString());

    }

}
