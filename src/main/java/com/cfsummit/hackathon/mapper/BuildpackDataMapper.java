package com.cfsummit.hackathon.mapper;

import com.cfsummit.hackathon.model.AppStats;
import com.cfsummit.hackathon.model.Org;
import com.cfsummit.hackathon.model.Space;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.applications.ApplicationEntity;
import org.cloudfoundry.client.v2.applications.ListApplicationsRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationManagersRequest;
import org.cloudfoundry.client.v2.organizations.ListOrganizationSpacesRequest;
import org.cloudfoundry.client.v2.users.ListUsersRequest;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.organizations.OrganizationInfoRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaltokarz on 13/06/2017.
 */
public class BuildpackDataMapper {

    private final CloudFoundryClient cloudFoundryClient;
    private final CloudFoundryOperations cloudFoundryOperations;

    public BuildpackDataMapper(CloudFoundryClient cloudFoundryClient, CloudFoundryOperations cloudFoundryOperations) {
        this.cloudFoundryClient = cloudFoundryClient;
        this.cloudFoundryOperations = cloudFoundryOperations;
    }

    public List<AppStats> map() {
        List<Org> orgs = new ArrayList<>();
        List<Space> spaces = new ArrayList<>();
        List<AppStats> appStatsList = new ArrayList<>();

        //Get organizations
        cloudFoundryOperations.organizations().list().toStream()
                .forEach(orgSummary -> {
                    Org org = new Org();
                    org.setId(orgSummary.getId());
                    org.setName(orgSummary.getName());

                    cloudFoundryClient.organizations().listManagers(
                            ListOrganizationManagersRequest.builder().organizationId(orgSummary.getId()).build())
                            .block()
                            .getResources()
                            .forEach(userResource -> org.addManager(userResource.getEntity().getUsername()));

                    orgs.add(org);

                });

        orgs.forEach(org -> {
            //Get spaces
            cloudFoundryClient.organizations().listSpaces(ListOrganizationSpacesRequest.builder()
                    .organizationId(org.getId()).build()).block().getResources()
                    .forEach(spaceResource -> spaces.add(
                            new Space(spaceResource.getMetadata().getId(),
                                    spaceResource.getEntity().getName())));

            //Get apps
            cloudFoundryClient.applicationsV2().list(ListApplicationsRequest.builder().organizationId(org.getId())
                    .build()).block()
                    .getResources()
                    .forEach(appResource -> {
                        AppStats appStats = new AppStats();
                        ApplicationEntity entity = appResource.getEntity();
                        appStats.setName(entity.getName());
                        appStats.setOrg(org.getName());
                        appStats.setSpace(spaces.stream()
                                .filter(space -> space.getId().equals(entity.getSpaceId()))
                                .findFirst().get()
                                .getName());
                        String buildpack = entity.getBuildpack() != null ?
                                entity.getBuildpack() : entity.getDetectedBuildpack();
                        appStats.setBuildpack(buildpack);

                        org.getOrgManagers().forEach(manager -> appStats.addManager(manager));

                        appStatsList.add(appStats);
                    });
        });

        return appStatsList;
    }
}
