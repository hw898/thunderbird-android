package app.k9mail.feature.account.setup

import app.k9mail.autodiscovery.api.AutoDiscoveryService
import app.k9mail.autodiscovery.service.RealAutoDiscoveryService
import app.k9mail.feature.account.common.featureAccountCommonModule
import app.k9mail.feature.account.oauth.featureAccountOAuthModule
import app.k9mail.feature.account.server.validation.featureAccountServerValidationModule
import app.k9mail.feature.account.setup.domain.DomainContract
import app.k9mail.feature.account.setup.domain.usecase.CreateAccount
import app.k9mail.feature.account.setup.domain.usecase.GetAutoDiscovery
import app.k9mail.feature.account.setup.ui.AccountSetupViewModel
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryContract
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryValidator
import app.k9mail.feature.account.setup.ui.autodiscovery.AccountAutoDiscoveryViewModel
import app.k9mail.feature.account.setup.ui.incoming.AccountIncomingConfigContract
import app.k9mail.feature.account.setup.ui.incoming.AccountIncomingConfigValidator
import app.k9mail.feature.account.setup.ui.incoming.AccountIncomingConfigViewModel
import app.k9mail.feature.account.setup.ui.options.AccountOptionsContract
import app.k9mail.feature.account.setup.ui.options.AccountOptionsValidator
import app.k9mail.feature.account.setup.ui.options.AccountOptionsViewModel
import app.k9mail.feature.account.setup.ui.outgoing.AccountOutgoingConfigContract
import app.k9mail.feature.account.setup.ui.outgoing.AccountOutgoingConfigValidator
import app.k9mail.feature.account.setup.ui.outgoing.AccountOutgoingConfigViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val featureAccountSetupModule: Module = module {
    includes(
        featureAccountCommonModule,
        featureAccountOAuthModule,
        featureAccountServerValidationModule,
    )

    single<OkHttpClient> {
        OkHttpClient()
    }

    single<AutoDiscoveryService> {
        RealAutoDiscoveryService(
            okHttpClient = get(),
        )
    }

    single<DomainContract.UseCase.GetAutoDiscovery> {
        GetAutoDiscovery(
            service = get(),
            oauthProvider = get(),
        )
    }

    factory<DomainContract.UseCase.CreateAccount> {
        CreateAccount(
            accountCreator = get(),
        )
    }

    factory<AccountAutoDiscoveryContract.Validator> { AccountAutoDiscoveryValidator() }
    factory<AccountIncomingConfigContract.Validator> { AccountIncomingConfigValidator() }
    factory<AccountOutgoingConfigContract.Validator> { AccountOutgoingConfigValidator() }
    factory<AccountOptionsContract.Validator> { AccountOptionsValidator() }

    viewModel {
        AccountSetupViewModel(
            createAccount = get(),
            accountStateRepository = get(),
        )
    }
    viewModel {
        AccountAutoDiscoveryViewModel(
            validator = get(),
            getAutoDiscovery = get(),
            accountStateRepository = get(),
            oAuthViewModel = get(),
        )
    }
    viewModel {
        AccountIncomingConfigViewModel(
            validator = get(),
            accountStateRepository = get(),
        )
    }

    viewModel {
        AccountOutgoingConfigViewModel(
            validator = get(),
            accountStateRepository = get(),
        )
    }

    viewModel {
        AccountOptionsViewModel(
            validator = get(),
            accountStateRepository = get(),
        )
    }
}
