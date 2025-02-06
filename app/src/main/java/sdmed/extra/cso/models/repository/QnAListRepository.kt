package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IQnAListRepository
import sdmed.extra.cso.interfaces.services.IQnAListService
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.qna.QnAContentModel
import sdmed.extra.cso.models.retrofit.qna.QnAHeaderModel
import sdmed.extra.cso.models.retrofit.qna.QnAReplyModel
import sdmed.extra.cso.utils.FExtensions

class QnAListRepository(private val _service: IQnAListService): IQnAListRepository {
    override suspend fun getList() = FExtensions.restTryT { _service.getList() }
    override suspend fun getHeaderData(thisPK: String) = FExtensions.restTryT { _service.getHeaderData(thisPK) }
    override suspend fun getContentData(thisPK: String) = FExtensions.restTryT { _service.getContentData(thisPK) }
    override suspend fun postData(title: String, qnaContentModel: QnAContentModel) = FExtensions.restTryT { _service.postData(title, qnaContentModel) }
    override suspend fun postReply(thisPK: String, qnaReplyModel: QnAReplyModel) = FExtensions.restTryT { _service.postReply(thisPK, qnaReplyModel) }
    override suspend fun putData(thisPK: String) = FExtensions.restTryT { _service.putData(thisPK) }
}