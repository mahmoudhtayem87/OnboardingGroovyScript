import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

ServiceContext serviceContext = (ServiceContext) workflowContext.get(WorkflowConstants.CONTEXT_SERVICE_CONTEXT);
def hostUrl = serviceContext.getPortalURL()

def serviceAccount = '<username>:<password>'
def encodedAccessToken = 'Basic ' + serviceAccount.bytes.encodeBase64().toString()
def post = new URL(hostUrl + "/o/c/insurancequotations/")
        .openConnection();

JSONObject message = JSONFactoryUtil.createJSONObject();
message.put("customerEmail",workflowContext.get('CustomerEmailAddress'));
message.put("customerId",workflowContext.get('CustomerID'));

message.put("manufacturingYear",workflowContext.get('Field38362142'));
message.put("policyType",workflowContext.get('Type'));

message.put("quotationData",workflowContext.get('data'));
message.put("quotationTotalValue",workflowContext.get('QuotationTotalValue'));


System.out.println(message)
post.setRequestMethod("POST")
post.setDoOutput(true)
post.setRequestProperty("Authorization", encodedAccessToken)
post.setRequestProperty("Content-Type", "application/json")
post.getOutputStream()
        .write(com.liferay.portal.kernel.json.JSONFactoryUtil.looseSerialize(message).getBytes("UTF-8"));
def postRC = post.getResponseCode();
System.out.println(postRC);
if (postRC.equals(200)) {
    System.out.println("Insurance Quotation has been created!");
}
com.liferay.portal.kernel.workflow.WorkflowStatusManagerUtil.updateStatus(com.liferay.portal.kernel.workflow.WorkflowConstants.getLabelStatus("approved"), workflowContext);

