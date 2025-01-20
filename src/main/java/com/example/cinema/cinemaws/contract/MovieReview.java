package com.example.cinema.cinemaws.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.2.
 */
@SuppressWarnings("rawtypes")
public class MovieReview extends Contract {
    public static final String BINARY = "0x6080604052348015600f57600080fd5b506113c88061001f6000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80635d3a1f9d146100675780637176cbcc14610083578063b03a850b146100a1578063e3125282146100d1578063e83ddcea146100ed578063eff5924f14610122575b600080fd5b610081600480360381019061007c9190610847565b610140565b005b61008b610156565b6040516100989190610924565b60405180910390f35b6100bb60048036038101906100b6919061097c565b6101e4565b6040516100c89190610baa565b60405180910390f35b6100eb60048036038101906100e69190610d28565b61046b565b005b6101076004803603810190610102919061097c565b610678565b60405161011996959493929190610dd8565b60405180910390f35b61012a610779565b6040516101379190610e40565b60405180910390f35b818160029182610151929190611072565b505050565b6002805461016390610e95565b80601f016020809104026020016040519081016040528092919081815260200182805461018f90610e95565b80156101dc5780601f106101b1576101008083540402835291602001916101dc565b820191906000526020600020905b8154815290600101906020018083116101bf57829003601f168201915b505050505081565b60606000805b60008054905081101561023f57836000828154811061020c5761020b611142565b5b9060005260206000209060050201600101540361023257818061022e906111a0565b9250505b80806001019150506101ea565b5060008167ffffffffffffffff81111561025c5761025b610bd1565b5b60405190808252806020026020018201604052801561029557816020015b61028261077f565b81526020019060019003908161027a5790505b5090506000805b60008054905081101561045f5785600082815481106102be576102bd611142565b5b9060005260206000209060050201600101540361045257600081815481106102e9576102e8611142565b5b90600052602060002090600502016040518060c0016040529081600082015481526020016001820154815260200160028201805461032690610e95565b80601f016020809104026020016040519081016040528092919081815260200182805461035290610e95565b801561039f5780601f106103745761010080835404028352916020019161039f565b820191906000526020600020905b81548152906001019060200180831161038257829003601f168201915b505050505081526020016003820160009054906101000a900460ff1660ff1660ff1681526020016003820160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200160048201548152505083838151811061043857610437611142565b5b6020026020010181905250818061044e906111a0565b9250505b808060010191505061029c565b50819350505050919050565b60018160ff1610158015610483575060058160ff1611155b6104c2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104b990611234565b60405180910390fd5b6000835111610506576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104fd906112a0565b60405180910390fd5b60006040518060c0016040528060015481526020018681526020018581526020018360ff1681526020013373ffffffffffffffffffffffffffffffffffffffff168152602001848152509080600181540180825580915050600190039060005260206000209060050201600090919091909150600082015181600001556020820151816001015560408201518160020190816105a291906112c0565b5060608201518160030160006101000a81548160ff021916908360ff16021790555060808201518160030160016101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060a0820151816004015550507f97a00dfa154a938656feb5f0ce7558fe26d39db8b125ce59628dcb0279a1dac8600154858584338760405161065296959493929190610dd8565b60405180910390a16001600081548092919061066d906111a0565b919050555050505050565b6000818154811061068857600080fd5b90600052602060002090600502016000915090508060000154908060010154908060020180546106b790610e95565b80601f01602080910402602001604051908101604052809291908181526020018280546106e390610e95565b80156107305780601f1061070557610100808354040283529160200191610730565b820191906000526020600020905b81548152906001019060200180831161071357829003601f168201915b5050505050908060030160009054906101000a900460ff16908060030160019054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060040154905086565b60015481565b6040518060c00160405280600081526020016000815260200160608152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081525090565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b600080fd5b60008083601f840112610807576108066107e2565b5b8235905067ffffffffffffffff811115610824576108236107e7565b5b6020830191508360018202830111156108405761083f6107ec565b5b9250929050565b6000806020838503121561085e5761085d6107d8565b5b600083013567ffffffffffffffff81111561087c5761087b6107dd565b5b610888858286016107f1565b92509250509250929050565b600081519050919050565b600082825260208201905092915050565b60005b838110156108ce5780820151818401526020810190506108b3565b60008484015250505050565b6000601f19601f8301169050919050565b60006108f682610894565b610900818561089f565b93506109108185602086016108b0565b610919816108da565b840191505092915050565b6000602082019050818103600083015261093e81846108eb565b905092915050565b6000819050919050565b61095981610946565b811461096457600080fd5b50565b60008135905061097681610950565b92915050565b600060208284031215610992576109916107d8565b5b60006109a084828501610967565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b6109de81610946565b82525050565b600082825260208201905092915050565b6000610a0082610894565b610a0a81856109e4565b9350610a1a8185602086016108b0565b610a23816108da565b840191505092915050565b600060ff82169050919050565b610a4481610a2e565b82525050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610a7582610a4a565b9050919050565b610a8581610a6a565b82525050565b600060c083016000830151610aa360008601826109d5565b506020830151610ab660208601826109d5565b5060408301518482036040860152610ace82826109f5565b9150506060830151610ae36060860182610a3b565b506080830151610af66080860182610a7c565b5060a0830151610b0960a08601826109d5565b508091505092915050565b6000610b208383610a8b565b905092915050565b6000602082019050919050565b6000610b40826109a9565b610b4a81856109b4565b935083602082028501610b5c856109c5565b8060005b85811015610b985784840389528151610b798582610b14565b9450610b8483610b28565b925060208a01995050600181019050610b60565b50829750879550505050505092915050565b60006020820190508181036000830152610bc48184610b35565b905092915050565b600080fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b610c09826108da565b810181811067ffffffffffffffff82111715610c2857610c27610bd1565b5b80604052505050565b6000610c3b6107ce565b9050610c478282610c00565b919050565b600067ffffffffffffffff821115610c6757610c66610bd1565b5b610c70826108da565b9050602081019050919050565b82818337600083830152505050565b6000610c9f610c9a84610c4c565b610c31565b905082815260208101848484011115610cbb57610cba610bcc565b5b610cc6848285610c7d565b509392505050565b600082601f830112610ce357610ce26107e2565b5b8135610cf3848260208601610c8c565b91505092915050565b610d0581610a2e565b8114610d1057600080fd5b50565b600081359050610d2281610cfc565b92915050565b60008060008060808587031215610d4257610d416107d8565b5b6000610d5087828801610967565b945050602085013567ffffffffffffffff811115610d7157610d706107dd565b5b610d7d87828801610cce565b9350506040610d8e87828801610967565b9250506060610d9f87828801610d13565b91505092959194509250565b610db481610946565b82525050565b610dc381610a2e565b82525050565b610dd281610a6a565b82525050565b600060c082019050610ded6000830189610dab565b610dfa6020830188610dab565b8181036040830152610e0c81876108eb565b9050610e1b6060830186610dba565b610e286080830185610dc9565b610e3560a0830184610dab565b979650505050505050565b6000602082019050610e556000830184610dab565b92915050565b600082905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b60006002820490506001821680610ead57607f821691505b602082108103610ec057610ebf610e66565b5b50919050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b600060088302610f287fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82610eeb565b610f328683610eeb565b95508019841693508086168417925050509392505050565b6000819050919050565b6000610f6f610f6a610f6584610946565b610f4a565b610946565b9050919050565b6000819050919050565b610f8983610f54565b610f9d610f9582610f76565b848454610ef8565b825550505050565b600090565b610fb2610fa5565b610fbd818484610f80565b505050565b5b81811015610fe157610fd6600082610faa565b600181019050610fc3565b5050565b601f82111561102657610ff781610ec6565b61100084610edb565b8101602085101561100f578190505b61102361101b85610edb565b830182610fc2565b50505b505050565b600082821c905092915050565b60006110496000198460080261102b565b1980831691505092915050565b60006110628383611038565b9150826002028217905092915050565b61107c8383610e5b565b67ffffffffffffffff81111561109557611094610bd1565b5b61109f8254610e95565b6110aa828285610fe5565b6000601f8311600181146110d957600084156110c7578287013590505b6110d18582611056565b865550611139565b601f1984166110e786610ec6565b60005b8281101561110f578489013582556001820191506020850194506020810190506110ea565b8683101561112c5784890135611128601f891682611038565b8355505b6001600288020188555050505b50505050505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60006111ab82610946565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82036111dd576111dc611171565b5b600182019050919050565b7f526174696e67206d757374206265206265747765656e203120616e6420350000600082015250565b600061121e601e8361089f565b9150611229826111e8565b602082019050919050565b6000602082019050818103600083015261124d81611211565b9050919050565b7f52657669657720746578742063616e6e6f7420626520656d7074790000000000600082015250565b600061128a601b8361089f565b915061129582611254565b602082019050919050565b600060208201905081810360008301526112b98161127d565b9050919050565b6112c982610894565b67ffffffffffffffff8111156112e2576112e1610bd1565b5b6112ec8254610e95565b6112f7828285610fe5565b600060209050601f83116001811461132a5760008415611318578287015190505b6113228582611056565b86555061138a565b601f19841661133886610ec6565b60005b828110156113605784890151825560018201915060208501945060208101905061133b565b8683101561137d5784890151611379601f891682611038565b8355505b6001600288020188555050505b50505050505056fea26469706673582212203c682dd5e653a20238d58aae1084e53bac1a581203d40e9e81bac21873d0068264736f6c634300081c0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDREVIEW = "addReview";

    public static final String FUNC_GETREVIEWSBYMOVIE = "getReviewsByMovie";

    public static final String FUNC_REVIEWCOUNT = "reviewCount";

    public static final String FUNC_REVIEWS = "reviews";

    public static final String FUNC_SETTEXT = "setText";

    public static final String FUNC_TEXTRAW = "textRaw";

    public static final Event REVIEWADDED_EVENT = new Event("ReviewAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint8>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected MovieReview(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MovieReview(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MovieReview(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MovieReview(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ReviewAddedEventResponse> getReviewAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(REVIEWADDED_EVENT, transactionReceipt);
        ArrayList<ReviewAddedEventResponse> responses = new ArrayList<ReviewAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ReviewAddedEventResponse typedResponse = new ReviewAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.movieId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.reviewText = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.rating = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.reviewer = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.reviewerId = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ReviewAddedEventResponse getReviewAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(REVIEWADDED_EVENT, log);
        ReviewAddedEventResponse typedResponse = new ReviewAddedEventResponse();
        typedResponse.log = log;
        typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.movieId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.reviewText = (String) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.rating = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.reviewer = (String) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.reviewerId = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
        return typedResponse;
    }

    public Flowable<ReviewAddedEventResponse> reviewAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getReviewAddedEventFromLog(log));
    }

    public Flowable<ReviewAddedEventResponse> reviewAddedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REVIEWADDED_EVENT));
        return reviewAddedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addReview(BigInteger _movieId, String _reviewText,
            BigInteger _reviewerId, BigInteger _rating) {
        final Function function = new Function(
                FUNC_ADDREVIEW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_movieId), 
                new org.web3j.abi.datatypes.Utf8String(_reviewText), 
                new org.web3j.abi.datatypes.generated.Uint256(_reviewerId), 
                new org.web3j.abi.datatypes.generated.Uint8(_rating)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getReviewsByMovie(BigInteger _movieId) {
        final Function function = new Function(FUNC_GETREVIEWSBYMOVIE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_movieId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Review>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> reviewCount() {
        final Function function = new Function(FUNC_REVIEWCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple6<BigInteger, BigInteger, String, BigInteger, String, BigInteger>> reviews(
            BigInteger param0) {
        final Function function = new Function(FUNC_REVIEWS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint8>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple6<BigInteger, BigInteger, String, BigInteger, String, BigInteger>>(function,
                new Callable<Tuple6<BigInteger, BigInteger, String, BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple6<BigInteger, BigInteger, String, BigInteger, String, BigInteger> call(
                            ) throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<BigInteger, BigInteger, String, BigInteger, String, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> setText(String _text) {
        final Function function = new Function(
                FUNC_SETTEXT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_text)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> textRaw() {
        final Function function = new Function(FUNC_TEXTRAW, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static MovieReview load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new MovieReview(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MovieReview load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MovieReview(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MovieReview load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new MovieReview(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MovieReview load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MovieReview(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MovieReview> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MovieReview.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<MovieReview> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MovieReview.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<MovieReview> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MovieReview.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<MovieReview> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MovieReview.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class Review extends DynamicStruct {
        public BigInteger id;

        public BigInteger movieId;

        public String reviewText;

        public BigInteger rating;

        public String reviewer;

        public BigInteger reviewerId;

        public Review(BigInteger id, BigInteger movieId, String reviewText, BigInteger rating,
                String reviewer, BigInteger reviewerId) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id), 
                    new org.web3j.abi.datatypes.generated.Uint256(movieId), 
                    new org.web3j.abi.datatypes.Utf8String(reviewText), 
                    new org.web3j.abi.datatypes.generated.Uint8(rating), 
                    new org.web3j.abi.datatypes.Address(160, reviewer), 
                    new org.web3j.abi.datatypes.generated.Uint256(reviewerId));
            this.id = id;
            this.movieId = movieId;
            this.reviewText = reviewText;
            this.rating = rating;
            this.reviewer = reviewer;
            this.reviewerId = reviewerId;
        }

        public Review(Uint256 id, Uint256 movieId, Utf8String reviewText, Uint8 rating,
                Address reviewer, Uint256 reviewerId) {
            super(id, movieId, reviewText, rating, reviewer, reviewerId);
            this.id = id.getValue();
            this.movieId = movieId.getValue();
            this.reviewText = reviewText.getValue();
            this.rating = rating.getValue();
            this.reviewer = reviewer.getValue();
            this.reviewerId = reviewerId.getValue();
        }
    }

    public static class ReviewAddedEventResponse extends BaseEventResponse {
        public BigInteger id;

        public BigInteger movieId;

        public String reviewText;

        public BigInteger rating;

        public String reviewer;

        public BigInteger reviewerId;
    }
}
