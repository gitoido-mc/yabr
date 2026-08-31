@file:Suppress("ktlint:standard:filename")

package lol.gito.yabr.helper

import com.mojang.datafixers.kinds.App
import com.mojang.serialization.codecs.RecordCodecBuilder

typealias RecordCodecDef<T> = (RecordCodecBuilder.Instance<T>) -> App<RecordCodecBuilder.Mu<T>, T>
