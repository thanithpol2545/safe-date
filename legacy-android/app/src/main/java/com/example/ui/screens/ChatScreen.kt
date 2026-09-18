package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.AppTab
import com.example.ui.HealthPulseUiState
import com.example.ui.HealthPulseViewModel
import com.example.ui.theme.*

@Composable
fun ChatScreen(
    state: HealthPulseUiState,
    viewModel: HealthPulseViewModel,
    modifier: Modifier = Modifier
) {
    if (state.activeChatConversation != null) {
        ChatRoomView(
            conversation = state.activeChatConversation,
            myTier = state.myTier,
            language = state.language,
            draftText = state.chatDraftMessage,
            isPartnerTyping = state.isPartnerTyping,
            onDraftChange = { viewModel.setChatDraft(it) },
            onSendMessage = { viewModel.sendCurrentChatMessage() },
            onSendIcebreaker = { viewModel.sendMessage(it) },
            onSendSafeDateProposal = { viewModel.sendSafeDateProposal() },
            onBack = { viewModel.closeChatRoom() },
            modifier = modifier
        )
    } else {
        ChatListView(
            state = state,
            onSelectConversation = { viewModel.openChatConversation(it) },
            onSelectMatch = { viewModel.openChatWithProfile(it) },
            onGoToDiscovery = { viewModel.setTab(AppTab.DISCOVERY) },
            modifier = modifier
        )
    }
}

@Composable
private fun ChatListView(
    state: HealthPulseUiState,
    onSelectConversation: (ChatConversation) -> Unit,
    onSelectMatch: (UserProfile) -> Unit,
    onGoToDiscovery: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // Section 1: New Matches Horizontal Story Bar
        item {
            Text(
                text = AppStrings.newMatchesHeader(state.language),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SlateGreyText,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (state.matchedProfilesList.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .testTag("matches_story_row")
                ) {
                    items(state.matchedProfilesList) { profile ->
                        MatchStoryAvatar(
                            profile = profile,
                            onClick = { onSelectMatch(profile) }
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = WarmCoral,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (state.language == AppLanguage.TH) "ปัดขวาเพื่อแมตช์กับโปรไฟล์ที่ผ่านการตรวจ" else "Swipe right to match with verified members",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateGreyMuted
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Section 2: Active Conversations List
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.conversationsHeader(state.language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SlateGreyText
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DeepTealLight,
                    border = BorderStroke(1.dp, DeepTealPrimary.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.VerifiedUser,
                            contentDescription = null,
                            tint = DeepTealPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (state.language == AppLanguage.TH) "ปลอดภัยด้วย ZKV" else "ZKV Protected",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepTealDark
                        )
                    }
                }
            }
        }

        if (state.conversations.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = WarmCoralLight,
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = null,
                                    tint = WarmCoral,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = AppStrings.noChatsYetTitle(state.language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SlateGreyText
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = AppStrings.noChatsYetSub(state.language),
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateGreyMuted,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onGoToDiscovery,
                            colors = ButtonDefaults.buttonColors(containerColor = WarmCoral)
                        ) {
                            Text(
                                text = if (state.language == AppLanguage.TH) "ไปหน้าหาคู่" else "Explore Matches",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            items(state.conversations) { conv ->
                ConversationItemCard(
                    conversation = conv,
                    language = state.language,
                    onClick = { onSelectConversation(conv) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun MatchStoryAvatar(
    profile: UserProfile,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clickable { onClick() }
            .testTag("match_story_${profile.id}")
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            // Ring frame matching tier
            Surface(
                shape = CircleShape,
                color = profile.avatarBgColor,
                border = BorderStroke(2.5.dp, profile.badgeTier.color),
                modifier = Modifier.size(60.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = profile.name.take(1),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Small badge tier emoji chip
            Surface(
                shape = CircleShape,
                color = Color.White,
                border = BorderStroke(1.dp, profile.badgeTier.color),
                modifier = Modifier.size(20.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(profile.badgeTier.emoji, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = profile.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Text(
            text = "${profile.age}",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = SlateGreyMuted
        )
    }
}

@Composable
private fun ConversationItemCard(
    conversation: ChatConversation,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val partner = conversation.partnerProfile
    val lastMsg = conversation.messages.lastOrNull()
    val isUnread = conversation.unreadCount > 0

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnread) WarmCreamSurface else PureWhiteSurface
        ),
        border = BorderStroke(
            1.dp,
            if (isUnread) WarmCoral.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("chat_item_${partner.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar with Tier Ring
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    shape = CircleShape,
                    color = partner.avatarBgColor,
                    border = BorderStroke(2.dp, partner.badgeTier.color),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = partner.name.take(1),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(1.dp, partner.badgeTier.color),
                    modifier = Modifier.size(18.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(partner.badgeTier.emoji, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${partner.name}, ${partner.age}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold,
                            color = SlateGreyText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Verified badge chip
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = partner.badgeTier.bgColor,
                            border = BorderStroke(0.8.dp, partner.badgeTier.color.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = partner.badgeTier.emoji,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }

                    Text(
                        text = lastMsg?.timestamp ?: conversation.matchedDate,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = if (isUnread) WarmCoral else SlateGreyMuted,
                        fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lastMsg?.text ?: if (language == AppLanguage.TH) "เริ่มบทสนทนาที่ปลอดภัย..." else "Start a safe conversation...",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isUnread) SlateGreyText else SlateGreyMuted,
                        fontWeight = if (isUnread) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (isUnread) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = WarmCoral,
                            modifier = Modifier.size(18.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${conversation.unreadCount}",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatRoomView(
    conversation: ChatConversation,
    myTier: BadgeTier,
    language: AppLanguage,
    draftText: String,
    isPartnerTyping: Boolean,
    onDraftChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onSendIcebreaker: (String) -> Unit,
    onSendSafeDateProposal: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val partner = conversation.partnerProfile
    val listState = rememberLazyListState()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(conversation.messages.size, isPartnerTyping) {
        if (conversation.messages.isNotEmpty()) {
            listState.animateScrollToItem(conversation.messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamSurface)
    ) {
        // Chat Room Header
        Surface(
            color = PureWhiteSurface,
            tonalElevation = 3.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("chat_back_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepTealDark
                    )
                }

                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        shape = CircleShape,
                        color = partner.avatarBgColor,
                        border = BorderStroke(2.dp, partner.badgeTier.color),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = partner.name.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(1.dp, partner.badgeTier.color),
                        modifier = Modifier.size(16.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(partner.badgeTier.emoji, fontSize = 9.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${partner.name}, ${partner.age}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SlateGreyText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.Filled.Verified,
                            contentDescription = "Verified",
                            tint = DeepTealPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = SafeGreen,
                            modifier = Modifier.size(6.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.chatOnlineStatus(language),
                            fontSize = 11.sp,
                            color = SafeGreen
                        )
                        Text(
                            text = " • ${partner.partnerClinicName ?: "HIS Verified"}",
                            fontSize = 11.sp,
                            color = SlateGreyMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Tier Pill Tag
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = partner.badgeTier.bgColor,
                    border = BorderStroke(1.dp, partner.badgeTier.color.copy(alpha = 0.5f)),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(partner.badgeTier.emoji, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = partner.badgeTier.shortName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = partner.badgeTier.color
                        )
                    }
                }
            }
        }

        // Messages Flow
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            // Pinned Mutual Verification Shield Card
            item {
                MutualHealthVerificationBanner(
                    partner = partner,
                    myTier = myTier,
                    language = language
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Messages
            items(conversation.messages) { message ->
                if (message.isSystemSafetyCard) {
                    SystemSafetyBubble(text = message.text)
                } else if (message.isSafeDateProposal) {
                    SafeDateProposalBubble(
                        text = message.text,
                        timestamp = message.timestamp,
                        isFromMe = message.isFromMe,
                        language = language
                    )
                } else {
                    ChatMessageBubble(
                        message = message,
                        partnerAvatarBg = partner.avatarBgColor,
                        partnerName = partner.name
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Partner Typing Indicator
            if (isPartnerTyping) {
                item {
                    PartnerTypingBubble(partnerName = partner.name, language = language)
                }
            }
        }

        // Suggested Icebreakers Bar
        Surface(
            color = PureWhiteSurface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                Text(
                    text = AppStrings.icebreakerTitle(language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepTealDark,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        ActionIcebreakerChip(
                            text = AppStrings.icebreaker1(language),
                            onClick = { onSendIcebreaker(AppStrings.icebreaker1(language)) }
                        )
                    }
                    item {
                        ActionIcebreakerChip(
                            text = AppStrings.icebreaker2(language),
                            onClick = { onSendIcebreaker(AppStrings.icebreaker2(language)) }
                        )
                    }
                    item {
                        ActionIcebreakerChip(
                            text = AppStrings.icebreaker3(language),
                            onClick = { onSendIcebreaker(AppStrings.icebreaker3(language)) }
                        )
                    }
                }
            }
        }

        // Bottom Input Row
        Surface(
            color = PureWhiteSurface,
            tonalElevation = 6.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Safe Date Proposal Button
                IconButton(
                    onClick = onSendSafeDateProposal,
                    modifier = Modifier.testTag("button_propose_safe_date")
                ) {
                    Icon(
                        Icons.Filled.LocalCafe,
                        contentDescription = "Propose Safe Date",
                        tint = WarmCoral,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Text Input
                OutlinedTextField(
                    value = draftText,
                    onValueChange = onDraftChange,
                    placeholder = {
                        Text(
                            AppStrings.chatInputPlaceholder(language),
                            fontSize = 13.sp,
                            color = SlateGreyMuted
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = WarmCreamSurface,
                        unfocusedContainerColor = WarmCreamSurface,
                        focusedBorderColor = DeepTealPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field")
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send Button
                IconButton(
                    onClick = onSendMessage,
                    enabled = draftText.isNotBlank(),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (draftText.isNotBlank()) WarmCoral else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (draftText.isNotBlank()) Color.White else SlateGreyMuted
                    ),
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MutualHealthVerificationBanner(
    partner: UserProfile,
    myTier: BadgeTier,
    language: AppLanguage
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DeepTealDark),
        border = BorderStroke(1.5.dp, DeepTealPrimary.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Security,
                    contentDescription = null,
                    tint = SafeGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.chatMutualSafetyTitle(language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = AppStrings.chatMutualSafetyDesc(
                    myTier = myTier.shortName,
                    partnerTier = partner.badgeTier.shortName,
                    partnerClinic = partner.partnerClinicName ?: "Hospital HIS",
                    lang = language
                ),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Color(0xFFE2E8F0),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("คุณ: ${myTier.emoji} ${myTier.shortName}", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("  ⟷  ", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    Text("${partner.name}: ${partner.badgeTier.emoji} ${partner.badgeTier.shortName}", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SafeGreen.copy(alpha = 0.2f),
                    border = BorderStroke(0.8.dp, SafeGreen)
                ) {
                    Text(
                        text = "Zero-Knowledge",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafeGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SystemSafetyBubble(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = null,
                    tint = DeepTealPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = SlateGreyText,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SafeDateProposalBubble(
    text: String,
    timestamp: String,
    isFromMe: Boolean,
    language: AppLanguage
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isFromMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = WarmCoralLight),
            border = BorderStroke(1.5.dp, WarmCoral.copy(alpha = 0.5f)),
            modifier = Modifier
                .widthIn(max = 290.dp)
                .testTag("safe_date_proposal_bubble")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = WarmCoral,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Filled.LocalCafe,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = AppStrings.chatSafeDateCardTitle(language),
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
                            fontWeight = FontWeight.Bold,
                            color = WarmCoralDark
                        )
                        Text(
                            text = "Safe Public Space",
                            fontSize = 10.sp,
                            color = SlateGreyMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = SlateGreyText,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timestamp,
                        fontSize = 10.sp,
                        color = SlateGreyMuted
                    )
                    if (isFromMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Filled.DoneAll,
                            contentDescription = "Delivered",
                            tint = WarmCoral,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    partnerAvatarBg: Color,
    partnerName: String
) {
    val isMe = message.isFromMe

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) {
            Surface(
                shape = CircleShape,
                color = partnerAvatarBg,
                modifier = Modifier
                    .size(28.dp)
                    .padding(bottom = 2.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = partnerName.take(1),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isMe) 18.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 18.dp
            ),
            color = if (isMe) DeepTealPrimary else PureWhiteSurface,
            border = if (!isMe) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) else null,
            shadowElevation = 1.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = if (isMe) Color.White else SlateGreyText,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.timestamp,
                        fontSize = 10.sp,
                        color = if (isMe) Color.White.copy(alpha = 0.7f) else SlateGreyMuted
                    )

                    if (isMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Filled.DoneAll,
                            contentDescription = "Delivered",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PartnerTypingBubble(
    partnerName: String,
    language: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = PureWhiteSurface,
            border = BorderStroke(1.dp, DeepTealLight),
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 1.5.dp,
                    color = DeepTealPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.TH) "$partnerName กำลังพิมพ์..." else "$partnerName is typing...",
                    fontSize = 11.sp,
                    color = SlateGreyMuted
                )
            }
        }
    }
}

@Composable
private fun ActionIcebreakerChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = WarmCreamSurface,
        border = BorderStroke(1.dp, DeepTealPrimary.copy(alpha = 0.3f)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 11.sp,
                color = DeepTealDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
