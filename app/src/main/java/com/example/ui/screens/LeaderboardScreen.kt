package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.components.LearnerAvatar
import com.example.ui.theme.BronzeMedal
import com.example.ui.theme.GoldMedal
import com.example.ui.theme.SilverMedal
import com.example.ui.viewmodel.LeaderboardEntry
import com.example.ui.viewmodel.VidyaQuestUiState

@Composable
fun LeaderboardScreen(
    state: VidyaQuestUiState.Ready,
    onOpenAddLearner: () -> Unit,
    onSwitchProfile: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var deviceOnly by remember { mutableStateOf(false) }

    val displayedEntries = remember(state.leaderboard, deviceOnly) {
        if (!deviceOnly) state.leaderboard
        else state.leaderboard
            .filter { it.learner.isUserControlled }
            .mapIndexed { idx, entry -> entry.copy(rank = idx + 1) }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(key = "trophy_banner") {
            LeaderboardHeaderBanner(onAddLearner = onOpenAddLearner)
        }

        item(key = "filter_row") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = !deviceOnly,
                        onClick = { deviceOnly = false },
                        label = { Text(stringResource(R.string.tab_all_learners)) },
                        modifier = Modifier.testTag("leaderboard_tab_all")
                    )
                    FilterChip(
                        selected = deviceOnly,
                        onClick = { deviceOnly = true },
                        label = { Text(stringResource(R.string.tab_device_profiles)) },
                        modifier = Modifier.testTag("leaderboard_tab_device")
                    )
                }
            }
        }

        if (displayedEntries.size >= 3) {
            item(key = "podium") {
                PodiumSection(
                    first = displayedEntries[0],
                    second = displayedEntries[1],
                    third = displayedEntries[2],
                    activeLearnerId = state.activeLearner.id
                )
            }
        }

        items(
            items = displayedEntries,
            key = { it.learner.id }
        ) { entry ->
            LeaderboardRankCard(
                entry = entry,
                isActiveLearner = entry.learner.id == state.activeLearner.id,
                onSwitchProfile = { onSwitchProfile(entry.learner.id) }
            )
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }
    }
}

@Composable
private fun LeaderboardHeaderBanner(onAddLearner: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_trophy_banner),
                contentDescription = stringResource(R.string.cd_trophy_banner),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x440F172A),
                                Color(0xE60B1220)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = stringResource(R.string.leaderboard_offline_note),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.leaderboard_title),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.leaderboard_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 2
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    FilledTonalButton(
                        onClick = onAddLearner,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("leaderboard_add_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PersonAdd,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.btn_add_learner))
                    }
                }
            }
        }
    }
}

@Composable
private fun PodiumSection(
    first: LeaderboardEntry,
    second: LeaderboardEntry,
    third: LeaderboardEntry,
    activeLearnerId: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            PodiumSlot(
                entry = second,
                medalColor = SilverMedal,
                podiumHeight = 62.dp,
                isActive = second.learner.id == activeLearnerId,
                modifier = Modifier.weight(1f)
            )
            PodiumSlot(
                entry = first,
                medalColor = GoldMedal,
                podiumHeight = 84.dp,
                isActive = first.learner.id == activeLearnerId,
                modifier = Modifier.weight(1f)
            )
            PodiumSlot(
                entry = third,
                medalColor = BronzeMedal,
                podiumHeight = 48.dp,
                isActive = third.learner.id == activeLearnerId,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PodiumSlot(
    entry: LeaderboardEntry,
    medalColor: Color,
    podiumHeight: Dp,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (entry.rank == 1) {
            Icon(
                imageVector = Icons.Rounded.EmojiEvents,
                contentDescription = null,
                tint = GoldMedal,
                modifier = Modifier.size(22.dp)
            )
        }
        LearnerAvatar(
            displayName = entry.learner.displayName,
            avatarIndex = entry.learner.avatarIndex,
            size = if (entry.rank == 1) 50.dp else 42.dp
        )
        Text(
            text = entry.learner.displayName,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(podiumHeight)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(medalColor.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "#${entry.rank}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = medalColor
                )
                Text(
                    text = "${entry.learner.totalXp} XP",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LeaderboardRankCard(
    entry: LeaderboardEntry,
    isActiveLearner: Boolean,
    onSwitchProfile: () -> Unit
) {
    val rankColor = when (entry.rank) {
        1 -> GoldMedal
        2 -> SilverMedal
        3 -> BronzeMedal
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_${entry.learner.id}"),
        shape = MaterialTheme.shapes.large,
        color = if (isActiveLearner) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
        else MaterialTheme.colorScheme.surface,
        border = if (isActiveLearner) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
        else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        tonalElevation = if (isActiveLearner) 3.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "#${entry.rank}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = rankColor,
                modifier = Modifier.width(32.dp)
            )

            LearnerAvatar(
                displayName = entry.learner.displayName,
                avatarIndex = entry.learner.avatarIndex,
                size = 42.dp
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = entry.learner.displayName,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (isActiveLearner) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = stringResource(R.string.badge_you),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    } else if (!entry.learner.isUserControlled) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = stringResource(R.string.badge_peer),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "LVL ${entry.levelInfo.level}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WorkspacePremium,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${entry.badgeCount} badges",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${entry.learner.totalXp}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (!isActiveLearner && entry.learner.isUserControlled) {
                    TextButton(
                        onClick = onSwitchProfile,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.testTag("study_as_${entry.learner.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SwapHoriz,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.action_switch_active),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
