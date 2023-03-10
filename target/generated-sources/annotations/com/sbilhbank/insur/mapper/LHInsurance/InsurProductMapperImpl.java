package com.sbilhbank.insur.mapper.LHInsurance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-01T14:28:59+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class InsurProductMapperImpl extends InsurProductMapper {

    @Override
    public InsurProduct insurProductDtoToInsurProduct(InsurProductDto insurProductDto) {
        if ( insurProductDto == null ) {
            return null;
        }

        InsurProduct.InsurProductBuilder<?, ?> insurProduct = InsurProduct.builder();

        insurProduct.id( insurProductDto.getRequestId() );
        try {
            if ( insurProductDto.getDob() != null ) {
                insurProduct.dob( new SimpleDateFormat( "dd-MMM-yyyy" ).parse( insurProductDto.getDob() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        try {
            if ( insurProductDto.getStartDate() != null ) {
                insurProduct.startDate( new SimpleDateFormat( "dd-MMM-yyyy" ).parse( insurProductDto.getStartDate() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        try {
            if ( insurProductDto.getEndDate() != null ) {
                insurProduct.endDate( new SimpleDateFormat( "dd-MMM-yyyy" ).parse( insurProductDto.getEndDate() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        try {
            if ( insurProductDto.getRequestDt() != null ) {
                insurProduct.requestDt( new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss" ).parse( insurProductDto.getRequestDt() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        insurProduct.txnType( insurProductDto.getTxnType() );
        if ( insurProductDto.getProjectId() != null ) {
            insurProduct.projectId( Integer.parseInt( insurProductDto.getProjectId() ) );
        }
        if ( insurProductDto.getProjectType() != null ) {
            insurProduct.projectType( Integer.parseInt( insurProductDto.getProjectType() ) );
        }
        insurProduct.branch( insurProductDto.getBranch() );
        insurProduct.cif( insurProductDto.getCif() );
        insurProduct.insuredNameEn( insurProductDto.getInsuredNameEn() );
        insurProduct.insuredNameKh( insurProductDto.getInsuredNameKh() );
        insurProduct.address( insurProductDto.getAddress() );
        insurProduct.gender( insurProductDto.getGender() );
        insurProduct.nidNo( insurProductDto.getNidNo() );
        insurProduct.accountNo( insurProductDto.getAccountNo() );
        insurProduct.phone( insurProductDto.getPhone() );
        insurProduct.occupation( insurProductDto.getOccupation() );
        insurProduct.sumInsured( insurProductDto.getSumInsured() );
        insurProduct.premium( insurProductDto.getPremium() );
        insurProduct.sysId( insurProductDto.getSysId() );
        insurProduct.status( insurProductDto.getStatus() );
        insurProduct.policyNo( insurProductDto.getPolicyNo() );

        return insurProduct.build();
    }

    @Override
    public List<InsurProduct> insurProductDtosToInsurProducts(List<InsurProductDto> insurProductDto) {
        if ( insurProductDto == null ) {
            return null;
        }

        List<InsurProduct> list = new ArrayList<InsurProduct>( insurProductDto.size() );
        for ( InsurProductDto insurProductDto1 : insurProductDto ) {
            list.add( insurProductDtoToInsurProduct( insurProductDto1 ) );
        }

        return list;
    }

    @Override
    public InsurProductDto insurProductToInsurProductDto(InsurProduct insurProduct) {
        if ( insurProduct == null ) {
            return null;
        }

        InsurProductDto.InsurProductDtoBuilder<?, ?> insurProductDto = InsurProductDto.builder();

        insurProductDto.requestId( insurProduct.getId() );
        if ( insurProduct.getDob() != null ) {
            insurProductDto.dob( new SimpleDateFormat( "dd-MMM-yyyy" ).format( insurProduct.getDob() ) );
        }
        if ( insurProduct.getStartDate() != null ) {
            insurProductDto.startDate( new SimpleDateFormat( "dd-MMM-yyyy" ).format( insurProduct.getStartDate() ) );
        }
        if ( insurProduct.getEndDate() != null ) {
            insurProductDto.endDate( new SimpleDateFormat( "dd-MMM-yyyy" ).format( insurProduct.getEndDate() ) );
        }
        if ( insurProduct.getRequestDt() != null ) {
            insurProductDto.requestDt( new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss" ).format( insurProduct.getRequestDt() ) );
        }
        insurProductDto.txnType( insurProduct.getTxnType() );
        if ( insurProduct.getProjectId() != null ) {
            insurProductDto.projectId( String.valueOf( insurProduct.getProjectId() ) );
        }
        if ( insurProduct.getProjectType() != null ) {
            insurProductDto.projectType( String.valueOf( insurProduct.getProjectType() ) );
        }
        insurProductDto.branch( insurProduct.getBranch() );
        insurProductDto.cif( insurProduct.getCif() );
        insurProductDto.insuredNameEn( insurProduct.getInsuredNameEn() );
        insurProductDto.insuredNameKh( insurProduct.getInsuredNameKh() );
        insurProductDto.address( insurProduct.getAddress() );
        insurProductDto.gender( insurProduct.getGender() );
        insurProductDto.nidNo( insurProduct.getNidNo() );
        insurProductDto.accountNo( insurProduct.getAccountNo() );
        insurProductDto.phone( insurProduct.getPhone() );
        insurProductDto.occupation( insurProduct.getOccupation() );
        insurProductDto.sumInsured( insurProduct.getSumInsured() );
        insurProductDto.premium( insurProduct.getPremium() );
        insurProductDto.sysId( insurProduct.getSysId() );
        insurProductDto.status( insurProduct.getStatus() );
        insurProductDto.policyNo( insurProduct.getPolicyNo() );

        return insurProductDto.build();
    }

    @Override
    public List<InsurProductDto> insurProductsToInsurProductDtos(List<InsurProduct> insurProduct) {
        if ( insurProduct == null ) {
            return null;
        }

        List<InsurProductDto> list = new ArrayList<InsurProductDto>( insurProduct.size() );
        for ( InsurProduct insurProduct1 : insurProduct ) {
            list.add( insurProductToInsurProductDto( insurProduct1 ) );
        }

        return list;
    }
}
