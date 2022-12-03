import com.liferay.portal.kernel.util.GetterUtil
import com.liferay.portal.kernel.workflow.WorkflowConstants
import com.liferay.portal.kernel.workflow.WorkflowHandler
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil
import com.liferay.portal.kernel.service.ServiceContext
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue
import com.liferay.asset.kernel.model.AssetRendererFactory
import com.liferay.asset.kernel.model.AssetRenderer
import com.liferay.portal.kernel.model.User
import com.liferay.portal.kernel.service.UserLocalServiceUtil
import java.util.List
import java.util.Locale
import com.liferay.custom.lifeinsurance.quotations.model.Quotation
import com.liferay.custom.lifeinsurance.quotations.service.QuotationLocalService
import com.liferay.portal.scripting.groovy.internal.GroovyExecutor
import org.osgi.framework.Bundle
import org.osgi.framework.FrameworkUtil
import org.osgi.util.tracker.ServiceTracker

ServiceTracker<QuotationLocalService, QuotationLocalService> st

try {
    long companyId = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID))
    ServiceContext serviceContext = (ServiceContext) workflowContext.get(WorkflowConstants.CONTEXT_SERVICE_CONTEXT)
    long classPK = GetterUtil.getLong((String) workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK))
    String className = (String) workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_NAME)
    WorkflowHandler workflowHandler = WorkflowHandlerRegistryUtil.getWorkflowHandler(className)
    AssetRendererFactory assetRendererFactory = workflowHandler.getAssetRendererFactory()
    AssetRenderer assetRenderer = workflowHandler.getAssetRenderer(classPK)
    Locale locale = serviceContext.getLocale()

    List<DDMFormFieldValue> values
    values = assetRenderer.getAssetObject().getDDMFormValues().getDDMFormFieldValues()

    String email = ''
    String customerId = ''
    String ManufacturingYear = ''
    String QuotationTotalValue = ''
    String CreatorId = "20127"
    String QuotationData = ""
    String PolicyType = ""

    for (DDMFormFieldValue value : values) {
        if (value.getDDMFormField().getLabel() != null && value.getValue() != null) {
            switch (value.getDDMFormField().getLabel().getString(locale)) {
                case 'Email':
                    email = value.getValue().getString(locale)
                    break
                case 'Customer ID':
                    customerId = value.getValue().getString(locale)
                    break
                case 'Quotation Total Value':
                    QuotationTotalValue = value.getValue().getString(locale)
                    break
                case 'Type':
                    PolicyType = value.getValue().getString(locale)
                    break
                default:
                    QuotationData += value.getDDMFormField().getLabel().getString(locale) + ":" + value.getValue().getString(locale) + ";"
                    break;
            }
        }
    }
    Bundle bundle = FrameworkUtil.getBundle(GroovyExecutor.class)
    st = new ServiceTracker(bundle.getBundleContext(), QuotationLocalService.class, null)
    st.open()
    QuotationLocalService jaService = st.waitForService(500)
    if (jaService == null) {
        _log.warn("The required service 'QuotationLocalService' is not available.")
    } else {
        Quotation quotation =
                jaService.addQuotation(CreatorId, companyId + "", PolicyType, QuotationData, QuotationTotalValue, customerId)
        java.util.List<Quotation> Quotations = jaService.getAllQuotations()
        System.out.print(quotation)
    }
    com.liferay.portal.kernel.workflow.WorkflowStatusManagerUtil.updateStatus(com.liferay.portal.kernel.workflow.WorkflowConstants.getLabelStatus("approved"), workflowContext);

}
catch (Exception e) {
    System.out.print(e)
}
finally {
    if (st != null) {
        st.close()
    }
}
