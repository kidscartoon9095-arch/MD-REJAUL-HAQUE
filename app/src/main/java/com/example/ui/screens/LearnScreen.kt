package com.example.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.curriculum.CurriculumCatalog
import com.example.data.local.LessonProgressEntity
import com.example.data.model.Lesson
import com.example.data.model.SubjectCategory
import com.example.ui.components.LearnerHeaderBar
import com.example.ui.components.subjectAccentColor
import com.example.ui.components.subjectIcon
import com.example.ui.viewmodel.VidyaQuestUiState

@Composable
fun LearnScreen(
    state: VidyaQuestUiState.Ready,
    onSelectSubjectFilter: (SubjectCategory?) -> Unit,
    onOpenLesson: (String) -> Unit,
    onOpenProfileDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visibleSubjects = remember(state.selectedSubjectFilter) {
        if (state.selectedSubjectFilter != null) listOf(state.selectedSubjectFilter)
        else SubjectCategory.entries
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "header") {
            LearnerHeaderBar(
                learner = state.activeLearner,
                levelInfo = state.levelInfo,
                onProfileClick = onOpenProfileDialog
            )
        }

        item(key = "hero_banner") {
            HeroQuestCard(
                recommendedLesson = state.recommendedLesson,
                allLessonsCompleted = state.allLessonsCompleted,
                completedCount = state.progressByLessonId.values.count { it.completed },
                totalLessons = CurriculumCatalog.lessons.size,
                onStartLesson = { onOpenLesson(state.recommendedLesson.id) }
            )
        }

        item(key = "subject_filters") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.section_subjects_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.selectedSubjectFilter == null,
                        onClick = { onSelectSubjectFilter(null) },
                        label = { Text(stringResource(R.string.filter_all_subjects)) },
                        modifier = Modifier.testTag("filter_chip_all")
                    )
                    SubjectCategory.entries.forEach { category ->
                        FilterChip(
                            selected = state.selectedSubjectFilter == category,
                            onClick = {
                                onSelectSubjectFilter(
                                    if (state.selectedSubjectFilter == category) null else category
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = subjectIcon(category),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            label = { Text(category.label) },
                            modifier = Modifier.testTag("filter_chip_${category.id}")
                        )
                    }
                }
            }
        }

        items(
            items = visibleSubjects,
            key = { it.id }
        ) { subject ->
            val trackLessons = CurriculumCatalog.lessonsForSubject(subject)
            SubjectTrackSection(
                subject = subject,
                lessons = trackLessons,
                progressByLessonId = state.progressByLessonId,
                onOpenLesson = onOpenLesson
            )
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }
    }
}

@Composable
private fun HeroQuestCard(
    recommendedLesson: Lesson,
    allLessonsCompleted: Boolean,
    completedCount: Int,
    totalLessons: Int,
    onStartLesson: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 3.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_learn),
                contentDescription = stringResource(R.string.cd_hero_banner),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x550F172A),
                                Color(0xE60B1220)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondary
                    ) {
                        Text(
                            text = stringResource(R.string.hero_kicker),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.18f)
                    ) {
                        Text(
                            text = "$completedCount / $totalLessons Lessons",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (allLessonsCompleted) stringResource(R.string.hero_title_mastered)
                        else stringResource(R.string.hero_title_continue),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFFDE68A)
                    )
                    Text(
                        text = recommendedLesson.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${recommendedLesson.subject.label} • +${recommendedLesson.baseXp} XP",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Button(
                            onClick = onStartLesson,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("hero_start_lesson_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (allLessonsCompleted) stringResource(R.string.hero_cta_review)
                                else stringResource(R.string.hero_cta_start)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubjectTrackSection(
    subject: SubjectCategory,
    lessons: List<Lesson>,
    progressByLessonId: Map<String, LessonProgressEntity>,
    onOpenLesson: (String) -> Unit
) {
    val accent = subjectAccentColor(subject)
    val completedInTrack = lessons.count { progressByLessonId[it.id]?.completed == true }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(accent.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = subjectIcon(subject),
                            contentDescription = subject.label,
                            tint = accent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = subject.label,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$completedInTrack of ${lessons.size} lessons completed",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = accent.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${(completedInTrack * 100 / lessons.size.coerceAtLeast(1))}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = accent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            lessons.forEach { lesson ->
                val progress = progressByLessonId[lesson.id]
                LessonRowItem(
                    lesson = lesson,
                    progress = progress,
                    accentColor = accent,
                    onClick = { onOpenLesson(lesson.id) }
                )
            }
        }
    }
}

@Composable
private fun LessonRowItem(
    lesson: Lesson,
    progress: LessonProgressEntity?,
    accentColor: Color,
    onClick: () -> Unit
) {
    val isCompleted = progress?.completed == true
    val borderColor = if (isCompleted) MaterialTheme.colorScheme.tertiary.copy(alpha = 0.45f)
    else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .border(1.dp, borderColor, MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .minimumInteractiveComponentSize()
            .testTag("lesson_card_${lesson.id}"),
        shape = MaterialTheme.shapes.medium,
        color = if (isCompleted) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.28f)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted) MaterialTheme.colorScheme.tertiary
                            else accentColor.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = stringResource(R.string.status_completed),
                            tint = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "${lesson.order}",
                            style = MaterialTheme.typography.titleSmall,
                            color = accentColor
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = lesson.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${lesson.durationMinutes} min",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Bolt,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = if (isCompleted && progress != null) {
                                    "Best ${progress.bestQuizScore}/${progress.totalQuestions}"
                                } else {
                                    "+${lesson.baseXp} XP"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }

            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
