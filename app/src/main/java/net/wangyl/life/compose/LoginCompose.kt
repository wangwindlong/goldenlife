package net.wangyl.life.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.compose.rememberNavController
import net.wangyl.life.obj.Global
import net.wangyl.life.vm.LoginViewModel
import timber.log.Timber

class LoginCompose : Fragment() {
    val viewModel by viewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                LoginScreen(viewModel)
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    MyComposeTheme(Global.theme) {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            NavigationActions(navController)
        }


        TextButton(onClick = {}) {
            
        }
        Button(onClick = {
            viewModel.login {
                Timber.d(" $it")
                Global.userSession = it
//                    .launchMain()
//                navigationActions.navigateToHome
            }
        }, Modifier.fillMaxWidth()) {
            Text(text = "登录", modifier = Modifier.size(100.dp))
        }

    }
}
