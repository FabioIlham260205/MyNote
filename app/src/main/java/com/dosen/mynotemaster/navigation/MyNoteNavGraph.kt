package com.dosen.mynotemaster.navigation



import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dosen.mynotemaster.ui.screens.AboutScreen
import com.dosen.mynotemaster.ui.screens.DashboardScreen
import com.dosen.mynotemaster.ui.screens.EditorScreen
import com.dosen.mynotemaster.viewmodel.NoteViewModel

/**
 * NavGraph = "peta jalan" aplikasi: layar apa saja yang ada
 * dan bagaimana cara berpindah di antaranya.
 */
@Composable
fun MyNoteNavGraph() {
    val navController = rememberNavController()

    // ViewModel dibuat DI SINI (level NavGraph), lalu dibagikan ke semua layar.
    val noteViewModel: NoteViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        // ── Layar 1: Dashboard ─────────────────────────────────────────
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = noteViewModel,
                onAddNote = {
                    navController.navigate(Screen.Editor.buildRoute())
                },
                onNoteClick = { noteId ->
                    navController.navigate(Screen.Editor.buildRoute(noteId))
                },
                onAboutClick = {
                    navController.navigate(Screen.About.route)
                }
            )
        }

        // ── Layar 2: Editor (Create & Edit) ────────────────────────────
        composable(
            route = Screen.Editor.route,
            arguments = listOf(
                navArgument(Screen.Editor.ARG_NOTE_ID) {
                    type = NavType.LongType
                    defaultValue = Screen.Editor.NO_ID
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments
                ?.getLong(Screen.Editor.ARG_NOTE_ID) ?: Screen.Editor.NO_ID

            EditorScreen(
                viewModel = noteViewModel,
                noteId = noteId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── Layar 3: About ─────────────────────────────────────────────
        composable(route = Screen.About.route) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}