package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.EducationDatabase
import com.example.data.repository.EducationRepository
import com.example.ui.components.CreateProfileDialog
import com.example.ui.components.ProfileManagerDialog
import com.example.ui.screens.BadgesScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LessonScreen
import com.example.ui.theme.VidyaQuestTheme
import com.example.ui.viewmodel.VidyaQuestUiState
import com.example.ui.viewmodel.VidyaQuestViewModel

private const val ROUTE_LEARN = "learn"
private const val ROUTE_BADGES = "badges"
private const val ROUTE_LEADERBOARD = "leaderboard"
private const val ROUTE_LESSON = "lesson/{lessonId}"

private data class TopLevelDestination(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val topLevelDestinations = listOf(
    TopLevelDestination(
        route = ROUTE_LEARN,
        labelRes = R.string.nav_learn,
        selectedIcon = Icons.Rounded.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook,
        testTag = "nav_learn"
    ),
    TopLevelDestination(
        route = ROUTE_BADGES,
        labelRes = R.string.nav_badges,
        selectedIcon = Icons.Rounded.WorkspacePremium,
        unselectedIcon = Icons.Outlined.WorkspacePremium,
        testTag = "nav_badges"
    ),
    TopLevelDestination(
        route = ROUTE_LEADERBOARD,
        labelRes = R.string.nav_leaderboard,
        selectedIcon = Icons.Rounded.EmojiEvents,
        unselectedIcon = Icons.Outlined.EmojiEvents,
        testTag = "nav_leaderboard"
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VidyaQuestTheme {
                VidyaQuestApp()
            }
        }
    }
}

@Composable
fun VidyaQuestApp() {
    val context = LocalContext.current.applicationContext
    val repository = remember {
        val db = EducationDatabase.getInstance(context)
        EducationRepository(db.educationDao())
    }
    val viewModel: VidyaQuestViewModel = viewModel(
        factory = VidyaQuestViewModel.Factory(repository)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lessonSession by viewModel.lessonSession.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showProfileDialog by remember { mutableStateOf(false) }
    var showCreateProfileDialog by remember { mutableStateOf(false) }

    val showChrome = currentRoute in topLevelDestinations.map { it.route }

    val navigateTopLevel: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    when (val readyState = uiState) {
        VidyaQuestUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is VidyaQuestUiState.Ready -> {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val useNavRail = maxWidth >= 600.dp

                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    bottomBar = {
                        if (showChrome && !useNavRail) {
                            NavigationBar(
                                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                            ) {
                                topLevelDestinations.forEach { dest ->
                                    val selected = currentRoute == dest.route
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(dest.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                                contentDescription = stringResource(dest.labelRes)
                                            )
                                        },
                                        label = { Text(stringResource(dest.labelRes)) },
                                        modifier = Modifier.testTag(dest.testTag)
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (showChrome && useNavRail) {
                            NavigationRail {
                                topLevelDestinations.forEach { dest ->
                                    val selected = currentRoute == dest.route
                                    NavigationRailItem(
                                        selected = selected,
                                        onClick = { navigateTopLevel(dest.route) },
                                        icon = {
                                            Icon(
                                                imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                                contentDescription = stringResource(dest.labelRes)
                                            )
                                        },
                                        label = { Text(stringResource(dest.labelRes)) },
                                        modifier = Modifier.testTag(dest.testTag)
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = ROUTE_LEARN,
                                modifier = Modifier
                                    .widthIn(max = 840.dp)
                                    .fillMaxSize()
                            ) {
                                composable(ROUTE_LEARN) {
                                    LearnScreen(
                                        state = readyState,
                                        onSelectSubjectFilter = viewModel::selectSubjectFilter,
                                        onOpenLesson = { lessonId ->
                                            viewModel.startLesson(lessonId)
                                            navController.navigate("lesson/$lessonId")
                                        },
                                        onOpenProfileDialog = { showProfileDialog = true }
                                    )
                                }
                                composable(ROUTE_BADGES) {
                                    BadgesScreen(state = readyState)
                                }
                                composable(ROUTE_LEADERBOARD) {
                                    LeaderboardScreen(
                                        state = readyState,
                                        onOpenAddLearner = { showCreateProfileDialog = true },
                                        onSwitchProfile = viewModel::switchProfile
                                    )
                                }
                                composable(
                                    route = ROUTE_LESSON,
                                    arguments = listOf(
                                        navArgument("lessonId") { type = NavType.StringType }
                                    )
                                ) { backStackEntry ->
                                    val lessonId = backStackEntry.arguments?.getString("lessonId")
                                    if (lessonSession == null && lessonId != null) {
                                        viewModel.startLesson(lessonId)
                                    }
                                    LessonScreen(
                                        session = lessonSession,
                                        onPreviousCard = viewModel::previousStudyCard,
                                        onNextCardOrQuiz = viewModel::nextStudyCardOrStartQuiz,
                                        onSelectOption = viewModel::selectQuizOption,
                                        onSubmitAnswer = viewModel::submitCurrentAnswer,
                                        onAdvanceQuiz = viewModel::advanceQuizOrFinish,
                                        onClose = {
                                            viewModel.clearLessonSession()
                                            navController.popBackStack()
                                        },
                                        onViewLeaderboard = {
                                            viewModel.clearLessonSession()
                                            navController.popBackStack()
                                            navigateTopLevel(ROUTE_LEADERBOARD)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showProfileDialog) {
                ProfileManagerDialog(
                    activeLearner = readyState.activeLearner,
                    deviceProfiles = readyState.deviceProfiles,
                    onDismiss = { showProfileDialog = false },
                    onSwitchProfile = viewModel::switchProfile,
                    onRenameActive = viewModel::renameActiveProfile,
                    onOpenCreateProfile = { showCreateProfileDialog = true }
                )
            }

            if (showCreateProfileDialog) {
                CreateProfileDialog(
                    onDismiss = { showCreateProfileDialog = false },
                    onCreate = viewModel::createProfile
                )
            }
        }
    }
}
