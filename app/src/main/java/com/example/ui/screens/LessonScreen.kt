package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.Lesson
import com.example.data.model.LessonCompletionResult
import com.example.data.model.QuizQuestion
import com.example.data.model.StudyCard
import com.example.ui.theme.GoldMedal
import com.example.ui.viewmodel.LessonPhase
import com.example.ui.viewmodel.LessonSessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    session: LessonSessionState?,
    onPreviousCard: () -> Unit,
    onNextCardOrQuiz: () -> Unit,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onAdvanceQuiz: () -> Unit,
    onClose: () -> Unit,
    onViewLeaderboard: () -> Unit
) {
    if (session == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val lesson = session.lesson
    val totalSteps = lesson.cards.size + lesson.quiz.size
    val currentStep = when (session.phase) {
        LessonPhase.STUDY -> session.studyCardIndex + 1
        LessonPhase.QUIZ -> lesson.cards.size + session.quizQuestionIndex + 1
        LessonPhase.RESULT -> totalSteps
    }
    val progress = (currentStep.toFloat() / totalSteps.coerceAtLeast(1)).coerceIn(0f, 1f)

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("close_lesson_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = stringResource(R.string.cd_close_lesson)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Text(
                                text = "+${lesson.baseXp} XP",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp),
                    strokeCap = StrokeCap.Round,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            AnimatedContent(
                targetState = session.phase,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "lesson_phase",
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 680.dp)
            ) { phase ->
                when (phase) {
                    LessonPhase.STUDY -> StudyCardsView(
                        lesson = lesson,
                        cardIndex = session.studyCardIndex,
                        card = lesson.cards[session.studyCardIndex],
                        onPrevious = onPreviousCard,
                        onNextOrQuiz = onNextCardOrQuiz
                    )
                    LessonPhase.QUIZ -> QuizView(
                        lesson = lesson,
                        questionIndex = session.quizQuestionIndex,
                        question = lesson.quiz[session.quizQuestionIndex],
                        selectedOption = session.selectedOptionIndex,
                        isSubmitted = session.isAnswerSubmitted,
                        comboCount = session.comboCount,
                        isSaving = session.isSavingResult,
                        onSelectOption = onSelectOption,
                        onSubmit = onSubmitAnswer,
                        onAdvance = onAdvanceQuiz
                    )
                    LessonPhase.RESULT -> LessonResultView(
                        result = session.completionResult,
                        onBackToCourses = onClose,
                        onViewLeaderboard = onViewLeaderboard
                    )
                }
            }
        }
    }
}

@Composable
private fun StudyCardsView(
    lesson: Lesson,
    cardIndex: Int,
    card: StudyCard,
    onPrevious: () -> Unit,
    onNextOrQuiz: () -> Unit
) {
    val isLastCard = cardIndex == lesson.cards.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "${stringResource(R.string.label_study_card)} ${cardIndex + 1} / ${lesson.cards.size}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Text(
                text = card.heading,
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = card.body,
                style = MaterialTheme.typography.bodyLarge
            )

            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_key_takeaway),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = card.keyTakeaway,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.label_real_world),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = card.realWorldExample,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (cardIndex > 0) {
                OutlinedButton(
                    onClick = onPrevious,
                    shape = CircleShape,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 50.dp)
                        .testTag("lesson_previous_card_button")
                ) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stringResource(R.string.btn_previous))
                }
            }
            Button(
                onClick = onNextOrQuiz,
                shape = CircleShape,
                modifier = Modifier
                    .weight(if (cardIndex > 0) 1.4f else 1f)
                    .heightIn(min = 50.dp)
                    .testTag("lesson_next_card_button")
            ) {
                Text(
                    text = if (isLastCard) stringResource(R.string.btn_start_quiz)
                    else stringResource(R.string.btn_next_card)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = if (isLastCard) Icons.Rounded.Quiz else Icons.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun QuizView(
    lesson: Lesson,
    questionIndex: Int,
    question: QuizQuestion,
    selectedOption: Int?,
    isSubmitted: Boolean,
    comboCount: Int,
    isSaving: Boolean,
    onSelectOption: (Int) -> Unit,
    onSubmit: () -> Unit,
    onAdvance: () -> Unit
) {
    val isLastQuestion = questionIndex == lesson.quiz.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "${stringResource(R.string.label_quiz_check)} ${questionIndex + 1} / ${lesson.quiz.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                if (comboCount >= 2) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.LocalFireDepartment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = stringResource(R.string.label_combo_bonus, comboCount),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }

            Text(
                text = question.prompt,
                style = MaterialTheme.typography.headlineMedium
            )

            question.options.forEachIndexed { index, optionText ->
                val isSelected = selectedOption == index
                val isCorrectOption = index == question.correctIndex
                val bgColor = when {
                    !isSubmitted && isSelected -> MaterialTheme.colorScheme.primaryContainer
                    isSubmitted && isCorrectOption -> MaterialTheme.colorScheme.tertiaryContainer
                    isSubmitted && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.errorContainer
                    else -> MaterialTheme.colorScheme.surface
                }
                val borderColor = when {
                    !isSubmitted && isSelected -> MaterialTheme.colorScheme.primary
                    isSubmitted && isCorrectOption -> MaterialTheme.colorScheme.tertiary
                    isSubmitted && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.outline
                }

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = bgColor,
                    border = BorderStroke(
                        width = if (isSelected || (isSubmitted && isCorrectOption)) 2.dp else 1.dp,
                        color = borderColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable(enabled = !isSubmitted) { onSelectOption(index) }
                        .testTag("quiz_option_$index")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(borderColor.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ('A' + index).toString(),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSubmitted && isCorrectOption) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = "Correct",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        } else if (isSubmitted && isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.Cancel,
                                contentDescription = "Incorrect",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = isSubmitted) {
                val isCorrect = selectedOption == question.correctIndex
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = if (isCorrect) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.65f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isCorrect) stringResource(R.string.feedback_correct, 15)
                            else stringResource(R.string.feedback_incorrect),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Button(
            onClick = { if (!isSubmitted) onSubmit() else onAdvance() },
            enabled = selectedOption != null && !isSaving,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .heightIn(min = 52.dp)
                .testTag(if (!isSubmitted) "quiz_submit_button" else "quiz_advance_button")
        ) {
            Text(
                text = when {
                    !isSubmitted -> "Check Answer"
                    isLastQuestion -> stringResource(R.string.btn_finish_lesson)
                    else -> stringResource(R.string.btn_next_question)
                }
            )
        }
    }
}

@Composable
private fun LessonResultView(
    result: LessonCompletionResult?,
    onBackToCourses: () -> Unit,
    onViewLeaderboard: () -> Unit
) {
    if (result == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(GoldMedal.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.EmojiEvents,
                contentDescription = "Lesson complete trophy",
                tint = GoldMedal,
                modifier = Modifier.size(48.dp)
            )
        }

        Text(
            text = if (result.accuracyPercent == 100) stringResource(R.string.result_title_amazing)
            else stringResource(R.string.result_title_good),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Text(
            text = "${result.lessonTitle} • ${stringResource(R.string.result_accuracy_label)}: ${result.correctAnswers}/${result.totalQuestions} (${result.accuracyPercent}%)",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.result_xp_earned, result.totalXpGained),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = GoldMedal
                    )
                }
                ResultLine(
                    label = if (result.isFirstCompletion) "Lesson Completion" else "Practice Review",
                    value = "+${result.baseXpAwarded} XP"
                )
                ResultLine(
                    label = "Quiz Performance Bonus",
                    value = "+${result.quizBonusXp} XP"
                )
                if (result.badgeBonusXp > 0) {
                    ResultLine(
                        label = "Milestone Badge Bonus",
                        value = "+${result.badgeBonusXp} XP"
                    )
                }
            }
        }

        if (result.newlyUnlockedBadges.isNotEmpty()) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
                border = BorderStroke(1.5.dp, GoldMedal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.result_badges_unlocked),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    result.newlyUnlockedBadges.forEach { badge ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(GoldMedal.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.WorkspacePremium,
                                    contentDescription = badge.title,
                                    tint = GoldMedal,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(badge.title, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    badge.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "+${badge.rewardXp} XP",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = onBackToCourses,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .testTag("result_back_to_courses_button")
        ) {
            Text(stringResource(R.string.btn_back_to_courses))
        }

        OutlinedButton(
            onClick = onViewLeaderboard,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp)
                .testTag("result_view_leaderboard_button")
        ) {
            Text(stringResource(R.string.btn_view_leaderboard))
        }
    }
}

@Composable
private fun ResultLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
